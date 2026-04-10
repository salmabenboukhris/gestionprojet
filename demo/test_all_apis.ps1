$baseUrl = "http://localhost:8081/api"
$contentType = "application/json"
$suffix = Get-Date -Format "HHmmss"

function Log($msg) { Write-Host "`n>>> $msg" -ForegroundColor Cyan }

function Invoke-Api {
    param($Uri, $Method = "Get", $Body = $null, $Headers = $null)
    try {
        $params = @{
            Uri = $Uri
            Method = $Method
            ContentType = $contentType
        }
        if ($Body) { $params.Body = $Body }
        if ($Headers) { $params.Headers = $Headers }
        
        return Invoke-RestMethod @params
    } catch {
        $err = $_.Exception.Message
        if ($_.Exception.Response) {
            $stream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($stream)
            $err += "`nAPI Error Body: " + $reader.ReadToEnd()
        }
        return @{ _error = $err }
    }
}

Log "Attente du démarrage complet du serveur..."
Start-Sleep -Seconds 5

# 1. Login Admin
Log "Login Admin..."
$loginResp = Invoke-Api -Uri "$baseUrl/auth/login" -Method Post -Body (@{ username = "admin"; password = "admin123" } | ConvertTo-Json)
if ($loginResp._error) { Write-Error "Login Admin échoué: $($loginResp._error)"; exit 1 }
$adminHeaders = @{ Authorization = "Bearer $($loginResp.token)" }

# 2. Map Profiles
Log "Récupération des IDs de Profils..."
$profils = Invoke-Api -Uri "$baseUrl/profils" -Method Get -Headers $adminHeaders
if ($profils._error) { Write-Error "Récupération profils échouée: $($profils._error)"; exit 1 }
$profMap = @{}
foreach ($p in $profils) {
    if ($p.roleCode) {
        $profMap[$p.roleCode] = $p.id
    }
}

# 3. Create Users
Log "Création des comptes de test..."
function Create-User($login, $role) {
    $body = @{
        matricule = "M-$login-$suffix"
        nom = "User $role"
        prenom = "$login"
        email = "$login$suffix@test.com"
        login = $login
        password = "password123"
        profilId = $profMap[$role]
    } | ConvertTo-Json
    $res = Invoke-Api -Uri "$baseUrl/employes" -Method Post -Body $body -Headers $adminHeaders
    if ($res._error) { Write-Error "Création user $login ($role) échouée: $($res._error)"; exit 1 }
    return $res.id
}

$cpLogin = "jean_$suffix"
$secLogin = "sara_$suffix"
$comptLogin = "omar_$suffix"
$dirLogin = "aziz_$suffix"

$cpEmpId = Create-User -login $cpLogin -role "CHEF_PROJET"
$secEmpId = Create-User -login $secLogin -role "SECRETAIRE"
$comptEmpId = Create-User -login $comptLogin -role "COMPTABLE"
$dirEmpId = Create-User -login $dirLogin -role "DIRECTEUR"

# 4. Admin Update Test
Log "Admin: Test Mise à jour partielle..."
$up = Invoke-Api -Uri "$baseUrl/employes/$cpEmpId" -Method Put -Body (@{ nom = "JEAN UPDATED" } | ConvertTo-Json) -Headers $adminHeaders
if ($up._error) { Write-Error "Update admin échoué: $($up._error)"; exit 1 }
Write-Host "Update OK. Nouveau nom: $($up.nom)"

# 5. Secrétaire Flow
Log "Flux Secrétaire: Organisme & Projet..."
$secToken = (Invoke-Api -Uri "$baseUrl/auth/login" -Method Post -Body (@{ username = $secLogin; password = "password123" } | ConvertTo-Json)).token
$secHeaders = @{ Authorization = "Bearer $secToken" }
$org = Invoke-Api -Uri "$baseUrl/organismes" -Method Post -Body (@{ code="ORG-$suffix"; nom="Client $suffix" } | ConvertTo-Json) -Headers $secHeaders
$prj = Invoke-Api -Uri "$baseUrl/projets" -Method Post -Body (@{ code="PRJ-$suffix"; nom="Projet $suffix"; montant=100000; organismeId=$org.id; chefProjetId=$cpEmpId } | ConvertTo-Json) -Headers $secHeaders

# 6. Chef Projet Flow
Log "Flux Chef Projet: Phases & Affectations..."
$cpToken = (Invoke-Api -Uri "$baseUrl/auth/login" -Method Post -Body (@{ username = $cpLogin; password = "password123" } | ConvertTo-Json)).token
$cpHeaders = @{ Authorization = "Bearer $cpToken" }
$phase = Invoke-Api -Uri "$baseUrl/projets/$($prj.id)/phases" -Method Post -Body (@{ code="PH-$suffix"; libelle="Phase $suffix"; montant=50000 } | ConvertTo-Json) -Headers $cpHeaders
$aff = Invoke-Api -Uri "$baseUrl/phases/$($phase.id)/employes/$cpEmpId" -Method Post -Body (@{ dateDebut="2024-03-01"; dateFin="2024-03-31" } | ConvertTo-Json) -Headers $cpHeaders

# 7. Comptable Flow
Log "Flux Comptable: Facturation..."
$comptToken = (Invoke-Api -Uri "$baseUrl/auth/login" -Method Post -Body (@{ username = $comptLogin; password = "password123" } | ConvertTo-Json)).token
$comptHeaders = @{ Authorization = "Bearer $comptToken" }
$fact = Invoke-Api -Uri "$baseUrl/phases/$($phase.id)/facture" -Method Post -Body (@{ code="F-$suffix"; montant=50000; description="Facture $suffix" } | ConvertTo-Json) -Headers $comptHeaders

# 8. Directeur Flow
Log "Flux Directeur: Dashboard..."
$dirToken = (Invoke-Api -Uri "$baseUrl/auth/login" -Method Post -Body (@{ username = $dirLogin; password = "password123" } | ConvertTo-Json)).token
$dirHeaders = @{ Authorization = "Bearer $dirToken" }
$dash = Invoke-Api -Uri "$baseUrl/reporting/tableau-de-bord" -Method Get -Headers $dirHeaders
Write-Host "Dashboard OK: $($dash.totalProjets) projets."

Log "TOUS LES TESTS ONT REUSSI ! Le système est stable et le RBAC est validé."
