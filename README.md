# Project_Staynow

## Спринт 5 Регресія і звіт про якість

Команда Backrooms. Рішення про готовність продукту — **No-Go** за критеріями плану.

| Матеріал | Файл |
|---|---|
| План регресії | [regression-plan.md](docs/sprint5/regression-plan.md) |
| Журнал | [regression-log.xlsx](docs/sprint5/regression-log.xlsx) |
| Матриця | [traceability-matrix.xlsx](docs/sprint5/traceability-matrix.xlsx) |
| Дефекти | [defects-regression.md](docs/sprint5/defects-regression.md) |
| Звіт | [DOCX](docs/sprint5/quality-report.docx), [PDF](docs/sprint5/quality-report.pdf) |
| Презентація | [presentation.pdf](docs/sprint5/presentation.pdf) |
| Рецензія та аудит | [review-checklist.md](docs/sprint5/review-checklist.md), [auditor-opinion.md](docs/sprint5/auditor-opinion.md) |
| Ретроспектива й задачі | [retro-semester.md](docs/sprint5/retro-semester.md), [tasks.md](docs/sprint5/tasks.md) |
| Докази прогонів | [v1](docs/sprint5/surefire-v1/), [v2](docs/sprint5/surefire-v2/) |
| Модуль v2 | [Maven-проєкт](code/modules/staynow/v2/) |

[Трекер GitHub Issues](https://github.com/MarkKmets/Project_Staynow/issues) містить дефекти модуля та нові дефекти v2. Поточні статуси демостенду зведено в реєстрі спринту 5.

155 виконань JUnit на v2: 146 Passed і 9 Failed. WebDriver: 9 Passed. Ручні: 9 Passed, 23 Failed і 17 Blocked. Загальний pass rate: 164/196 = 83,7%; частка виконаних: 196/213 = 92,0%.

## Запуск модуля v2

```bash
mvn -B -f code/modules/staynow/v2/pom.xml test
```

Звіти створюються в target/surefire-reports/ Maven-проєкту. Вихідні тести збережено без зміни очікувань.
