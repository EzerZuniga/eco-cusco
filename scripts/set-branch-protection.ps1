<#
Idempotent PowerShell script to apply a branch-protection rule to the repo using
the GitHub REST API. Requires a token with `repo` scope stored in `GITHUB_TOKEN`.

Usage (PowerShell):
$env:GITHUB_TOKEN = 'ghp_xxx'
.
\scripts\set-branch-protection.ps1

You can override owner/repo/branch by calling the script with parameters.
#>

param(
    [string] $Owner = "EzerZuniga",
    [string] $Repo = "eco-cusco",
    [string] $Branch = "main",
    [string] $Token = $env:GITHUB_TOKEN
)

if (-not $Token) {
    Write-Error "GITHUB_TOKEN environment variable not set. Export it before running, e.g. $env:GITHUB_TOKEN='ghp_xxx'"
    exit 1
}

$uri = "https://api.github.com/repos/$Owner/$Repo/branches/$Branch/protection"
$body = @{
    required_status_checks = @{
        strict = $true
        contexts = @("Maven CI")
    }
    enforce_admins = $true
    required_pull_request_reviews = @{
        dismiss_stale_reviews = $true
        require_code_owner_reviews = $true
        required_approving_review_count = 1
    }
    restrictions = $null
} | ConvertTo-Json -Depth 10

$headers = @{
    Authorization = "token $Token"
    Accept = "application/vnd.github+json"
    "User-Agent" = "set-branch-protection-script"
}

try {
    $response = Invoke-RestMethod -Uri $uri -Method Put -Headers $headers -Body $body -ContentType "application/json"
    Write-Host "Branch protection applied successfully."
} catch {
    Write-Error "Failed to apply branch protection: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $resp = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($resp)
        $text = $reader.ReadToEnd()
        Write-Error $text
    }
    exit 1
}
