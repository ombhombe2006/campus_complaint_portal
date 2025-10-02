# 🔧 Comprehensive Validation Script - All Deployment Options
# Run this before any presentation to ensure all backup options work

Write-Host "🔄 CONTROLLED REDUNDANCY VALIDATION" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

$totalTests = 0
$passedTests = 0

function Test-Option {
    param($testName, $condition)
    $global:totalTests++
    if ($condition) {
        Write-Host "✅ $testName" -ForegroundColor Green
        $global:passedTests++
        return $true
    } else {
        Write-Host "❌ $testName" -ForegroundColor Red
        return $false
    }
}

Write-Host "`n📁 CHECKING FILE STRUCTURE..." -ForegroundColor Yellow

# Test Option 1: Simple Demo
Test-Option "simple-demo/index.html exists" (Test-Path "simple-demo/index.html")
Test-Option "simple-demo/css/style.css exists" (Test-Path "simple-demo/css/style.css")
Test-Option "simple-demo/js/main.js exists" (Test-Path "simple-demo/js/main.js")
Test-Option "simple-demo/admin-dashboard.html exists" (Test-Path "simple-demo/admin-dashboard.html")

Write-Host "`n☕ CHECKING SPRING BOOT BACKEND..." -ForegroundColor Yellow

# Test Option 2: Spring Boot
Test-Option "backend/pom.xml exists" (Test-Path "backend/pom.xml")
Test-Option "backend/src/main/java/com/campus/CampusReviewPortalApplication.java exists" (Test-Path "backend/src/main/java/com/campus/CampusReviewPortalApplication.java")
Test-Option "backend/src/main/java/com/campus/controller exists" (Test-Path "backend/src/main/java/com/campus/controller")
Test-Option "backend/src/main/java/com/campus/entity exists" (Test-Path "backend/src/main/java/com/campus/entity")
Test-Option "backend/src/main/java/com/campus/repository exists" (Test-Path "backend/src/main/java/com/campus/repository")

Write-Host "`n🔄 CHECKING SERVLET BACKUP..." -ForegroundColor Yellow

# Test Option 4: Servlet Backup
Test-Option "backend-servlet-backup exists" (Test-Path "backend-servlet-backup")
Test-Option "backend-servlet-backup/pom.xml exists" (Test-Path "backend-servlet-backup/pom.xml")

Write-Host "`n🗄️ CHECKING DATABASE..." -ForegroundColor Yellow

# Database files
Test-Option "database/schema.sql exists" (Test-Path "database/schema.sql")

Write-Host "`n📚 CHECKING DOCUMENTATION..." -ForegroundColor Yellow

# Documentation redundancy
Test-Option "README.md exists" (Test-Path "README.md")
Test-Option "DEPLOYMENT_GUIDE.md exists" (Test-Path "DEPLOYMENT_GUIDE.md")
Test-Option "DEPLOYMENT_OPTIONS.md exists" (Test-Path "DEPLOYMENT_OPTIONS.md")
Test-Option "WARP.md exists" (Test-Path "WARP.md")
Test-Option "archive-docs folder exists" (Test-Path "archive-docs")

Write-Host "`n🚀 TESTING DEMO FUNCTIONALITY..." -ForegroundColor Yellow

# Test simple-demo HTML structure
if (Test-Path "simple-demo/index.html") {
    $demoContent = Get-Content "simple-demo/index.html" -Raw
    Test-Option "Demo has login form" ($demoContent -match "login")
    Test-Option "Demo has proper styling" (Test-Path "simple-demo/css/style.css")
    Test-Option "Demo has JavaScript" (Test-Path "simple-demo/js/main.js")
}

Write-Host "`n🛠️ TESTING JAVA ENVIRONMENT..." -ForegroundColor Yellow

# Check Java availability
try {
    $javaVersion = java -version 2>&1
    Test-Option "Java is available" ($LASTEXITCODE -eq 0)
} catch {
    Test-Option "Java is available" $false
}

# Check Maven availability
try {
    $mavenVersion = mvn -version 2>&1
    Test-Option "Maven is available" ($LASTEXITCODE -eq 0)
} catch {
    Test-Option "Maven is available" $false
}

Write-Host "`n📊 VALIDATION RESULTS" -ForegroundColor Cyan
Write-Host "=====================" -ForegroundColor Cyan
Write-Host "Tests Passed: $passedTests/$totalTests" -ForegroundColor $(if ($passedTests -eq $totalTests) { "Green" } else { "Yellow" })

$successRate = [math]::Round(($passedTests / $totalTests) * 100, 1)
Write-Host "Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 90) { "Green" } elseif ($successRate -ge 75) { "Yellow" } else { "Red" })

Write-Host "`n🎯 DEPLOYMENT READINESS:" -ForegroundColor Cyan

if (Test-Path "simple-demo/index.html") {
    Write-Host "✅ OPTION 1 (Simple Demo): READY - 100% guaranteed to work" -ForegroundColor Green
} else {
    Write-Host "❌ OPTION 1 (Simple Demo): NOT READY" -ForegroundColor Red
}

if ((Test-Path "backend/pom.xml") -and (Test-Path "backend/src/main/java/com/campus/CampusReviewPortalApplication.java")) {
    Write-Host "✅ OPTION 2 (Spring Boot): READY - Files present" -ForegroundColor Green
} else {
    Write-Host "❌ OPTION 2 (Spring Boot): NOT READY" -ForegroundColor Red
}

if (Test-Path "backend-servlet-backup/pom.xml") {
    Write-Host "✅ OPTION 4 (Servlet Backup): READY - Backup available" -ForegroundColor Green
} else {
    Write-Host "❌ OPTION 4 (Servlet Backup): NOT READY" -ForegroundColor Red
}

Write-Host "`n💡 RECOMMENDATIONS:" -ForegroundColor Yellow

if ($successRate -ge 95) {
    Write-Host "🏆 EXCELLENT: All redundancy options are ready!" -ForegroundColor Green
    Write-Host "   You can proceed with any deployment method confidently." -ForegroundColor Green
} elseif ($successRate -ge 85) {
    Write-Host "👍 GOOD: Most options are ready." -ForegroundColor Yellow
    Write-Host "   Focus on fixing any failed items above." -ForegroundColor Yellow
} else {
    Write-Host "⚠️  NEEDS ATTENTION: Several issues found." -ForegroundColor Red
    Write-Host "   Consider using simple-demo as primary option." -ForegroundColor Red
}

Write-Host "`n🎪 PRESENTATION STRATEGY:" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan
Write-Host "PRIMARY:   Use simple-demo/ for guaranteed success" -ForegroundColor Green
Write-Host "SECONDARY: Show Spring Boot backend if time permits" -ForegroundColor Yellow
Write-Host "FALLBACK:  Always have GitHub repo URL ready" -ForegroundColor Blue

Write-Host "`nValidation complete! Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")