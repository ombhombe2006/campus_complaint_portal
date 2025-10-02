# Pre-Deployment Test Script
# Run this before presentation day to ensure everything works

Write-Host "🔍 Campus Review Portal - Pre-Deployment Test" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Test 1: Java Version
Write-Host "`n✅ Testing Java..." -ForegroundColor Yellow
try {
    $javaVersion = java --version 2>$null
    if ($javaVersion) {
        Write-Host "✅ Java is available" -ForegroundColor Green
        Write-Host $javaVersion[0] -ForegroundColor Cyan
    } else {
        Write-Host "❌ Java not found" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Java not available" -ForegroundColor Red
}

# Test 2: Project Structure
Write-Host "`n✅ Testing Project Structure..." -ForegroundColor Yellow
$requiredPaths = @(
    "simple-demo\index.html",
    "frontend\index.html", 
    "backend\pom.xml",
    "database\schema.sql",
    "DEPLOYMENT_CHECKLIST.md",
    "QUICK_START.md"
)

foreach ($path in $requiredPaths) {
    if (Test-Path $path) {
        Write-Host "✅ $path exists" -ForegroundColor Green
    } else {
        Write-Host "❌ $path missing" -ForegroundColor Red
    }
}

# Test 3: Demo Mode
Write-Host "`n✅ Testing Demo Mode..." -ForegroundColor Yellow
if (Test-Path "simple-demo\index.html") {
    Write-Host "✅ Simple demo ready" -ForegroundColor Green
    Write-Host "  → Can open: simple-demo\index.html" -ForegroundColor Cyan
    Write-Host "  → Credentials: user1/user123, admin/admin123" -ForegroundColor Cyan
} else {
    Write-Host "❌ Simple demo not found" -ForegroundColor Red
}

# Test 4: Check if Maven is available (optional)
Write-Host "`n✅ Testing Maven (optional)..." -ForegroundColor Yellow
try {
    $mavenVersion = mvn --version 2>$null
    if ($mavenVersion) {
        Write-Host "✅ Maven is already available" -ForegroundColor Green
        Write-Host $mavenVersion[0] -ForegroundColor Cyan
    } else {
        Write-Host "⚠️  Maven not installed (will install on deployment day)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "⚠️  Maven not available (will install on deployment day)" -ForegroundColor Yellow
}

# Test 5: Check ports
Write-Host "`n✅ Testing Port Availability..." -ForegroundColor Yellow
$port8080 = netstat -an | Select-String ":8080"
if ($port8080) {
    Write-Host "⚠️  Port 8080 is in use - may need to stop service on deployment day" -ForegroundColor Yellow
    Write-Host $port8080 -ForegroundColor Cyan
} else {
    Write-Host "✅ Port 8080 is available" -ForegroundColor Green
}

# Summary
Write-Host "`n📋 PRE-DEPLOYMENT SUMMARY" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green
Write-Host "✅ Ready for INSTANT DEMO: Open simple-demo\index.html" -ForegroundColor Green
Write-Host "⚠️  For FULL DEPLOYMENT: Follow DEPLOYMENT_CHECKLIST.md" -ForegroundColor Yellow
Write-Host ""
Write-Host "🎯 Presentation Day Strategy:" -ForegroundColor Cyan
Write-Host "  1. Start with simple-demo for immediate impact" -ForegroundColor White
Write-Host "  2. If time permits, show full deployment process" -ForegroundColor White
Write-Host "  3. Always have simple-demo as backup" -ForegroundColor White

Write-Host "`nPress any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")