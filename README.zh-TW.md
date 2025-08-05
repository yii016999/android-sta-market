[View in English (README.md)](./README.md)

# Sta Market
Sta Market 是一款使用 Kotlin 和 Supabase 所開發的 Android 電商應用程式。  
本專案採用 MVVM 架構與 TDD 測試驅動開發流程，並整合 CI 機制以確保程式碼品質與穩定性。

[![Android CI](https://github.com/yii016999/android-sta-market/actions/workflows/android-ci.yml/badge.svg)](https://github.com/yii016999/sta-market/actions)

## 開發者設定
在你 `clone` 專案後，請執行以下指令以啟用 pre-commit 檢查機制：

```bash
./scripts/setup-hooks.sh
```

## 功能特色
- MVVM 架構與 Repository Pattern
- 採用 Kotlin 與 Jetpack Compose
- 整合 Supabase 作為後端（認證、資料庫）
- 以 TDD 測試驅動開發，並結合 CI 自動化測試
- 使用 detekt 進行靜態分析
- 使用 ktlint 維護一致的程式風格

## 專案狀態
本專案仍在開發中，後續將持續新增功能與補充文件。