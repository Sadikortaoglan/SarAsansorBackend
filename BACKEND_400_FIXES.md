# Backend 400 Bad Request Fixes

## Tespit Edilen Sorunlar

### 1. Fault Service - Enum Mismatch (CRITICAL)

**Problem:**
- Frontend gönderiyor: `status=ACIK` veya `status=TAMAMLANDI`
- Backend enum: `Fault.Status.OPEN` ve `Fault.Status.COMPLETED`
- Service'de `Fault.Status.valueOf(status.toUpperCase())` kullanılıyor
- `ACIK` veya `TAMAMLANDI` gönderildiğinde `IllegalArgumentException` fırlatıyor → 400 Bad Request

**Çözüm:**
- `FaultService.getFaultsByStatus()` metodunda Türkçe status değerlerini İngilizce enum'a map et
- `FaultService.updateFaultStatus()` metodunda da aynı mapping'i ekle

**Mapping:**
- `ACIK` → `OPEN`
- `TAMAMLANDI` → `COMPLETED`

### 2. Inspection Service - Result Field

**Durum:**
- Inspection.result String olarak saklanıyor (enum yok)
- Frontend `PASS`, `FAIL`, `PENDING` gönderiyor
- Backend validation var: `@NotBlank(message = "Result cannot be empty")`
- Şu an sorun yok gibi görünüyor, ama kontrol edilmeli

### 3. Maintenance Service - Query Parameters

**Durum:**
- `paid` (Boolean) ✅
- `dateFrom` (LocalDate) ✅
- `dateTo` (LocalDate) ✅
- `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)` kullanılıyor ✅
- Şu an sorun yok gibi görünüyor

### 4. Warning Controller

**Durum:**
- `type` query parameter: `EXPIRED`, `WARNING` ✅
- Frontend ile uyumlu ✅

## Yapılacak Düzeltmeler

1. ✅ FaultService.getFaultsByStatus() - ACIK/TAMAMLANDI mapping
2. ✅ FaultService.updateFaultStatus() - ACIK/TAMAMLANDI mapping
3. ⚠️ GlobalExceptionHandler - 400 error logging ekle
4. ⚠️ Test tüm endpoint'ler

