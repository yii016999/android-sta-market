[View in Traditional Chinese (README.zh-TW.md)](./README.zh-TW.md)

# Sta Market
Sta Market is an Android e-commerce application built with Kotlin and Supabase.  
It follows the MVVM architecture and test-driven development (TDD) workflow, and integrates
continuous integration (CI) for code quality and stability.

[![Android CI](https://github.com/yii016999/android-sta-market/actions/workflows/android-ci.yml/badge.svg)](https://github.com/yii016999/sta-market/actions)

## Developer Setup
To enable pre-commit checks for static analysis with detekt, run the following command **after cloning** the repository:

```bash
./scripts/setup-hooks.sh
```

## Features
- MVVM architecture with Repository Pattern
- Built with Kotlin and Jetpack Compose
- Supabase backend integration (Auth, Database)
- TDD-first development with CI testing
- Static code analysis with detekt
- Code style enforcement with ktlint

## Project Status
This project is under active development.  
More features and documentation will be added progressively.