# Автоматизація Досьє якості v4

**Команда:** Backrooms. **Продукт:** «СтейНау» (StayNow). **Спринт:** 4. **Версія:** 1.0.

[Репозиторій](https://github.com/MarkKmets/Project_Staynow), [трекер дефектів](https://github.com/MarkKmets/Project_Staynow/issues).

## Ролі

| Учасник | Роль | Зона відповідальності |
|---|---|---|
| Тіньков Данііл | Керівник тестування | План, розподіл тестів, зведення інкременту, демо |
| Кмець Марк | Тест-аналітик | Матриця BL, вибір UI-кейсів і простежуваність до SRS/TC |
| Гатченко Ярослав | Інженер з тестування | Інфраструктура, UI, Selenium IDE, запуск і збереження звітів |
| Сирота Володимир | Скрам-майстер і рецензент | Review, перевірка звітів, таблиця задач, ретро |
| Сімутін Владислав | Аудитор-опонент | Акт аудиту, відтворення, перевірка простежуваності |

## Нормативи план і факт

| Показник | Вимога для 5 осіб | Результат |
|---|---|---|
| Основні юніт-методи | ≥20 | 57 |
| Методів на учасника | ≥3 | 9 / 13 / 7 / 14 / 14 |
| UI-сценарії | ≥8 | 9; два успішні прогони |
| Дефекти модуля | ≥5 | 6, BUG-019…024 |
| Selenium IDE | 1–2 записи та Java-експорт | Три smoke-сценарії у .side, два Java-експорти; докази відтворення |

## Матеріали інкременту

| Артефакт | Розташування |
|---|---|
| Проєкт, вимоги до середовища, збірка, запуск і автори тестів | [qa-staynow/README.md](qa-staynow/README.md) |
| Матриця BL і зауваження review | [coverage-matrix.md](qa-staynow/docs/coverage-matrix.md) |
| Перелік UI-сценаріїв | [ui-scenarios.md](qa-staynow/docs/ui-scenarios.md) |
| Підсумок Surefire і failed → BUG | [surefire-summary.md](qa-staynow/docs/surefire-summary.md) |
| Звіти про дефекти модуля | [GitHub Issues](https://github.com/MarkKmets/Project_Staynow/issues), мітка module |
| Таблиця розподілу задач | [task-board.md](task-board.md) |
| Протокол ретроспективи | [retro.md](retro.md) |
| Протокол аудитора-опонента | [audit.md](audit.md) |

## Дефекти модуля

| Дефект | Вимога | Відтворення | Severity / Priority | GitHub |
|---|---|---|---|---|
| BUG-019 | BL-10 | `allowedNightsAreAccepted, 30 ночей` | Major / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-019) |
| BUG-020 | BL-10 | `checkInYesterdayCheckOutTomorrow` | Major / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-020) |
| BUG-021 | BL-30/50 | `weekendNight, nightPrice, total*` | Major / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-021) |
| BUG-022 | BL-50 | `nightPriceRoundsHalfUp*` | Minor / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-022) |
| BUG-023 | BL-60 | `cancellationFee і refund, 7 днів` | Major / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-023) |
| BUG-024 | BL-70 | `суміжні інтервали; backToBackBookingsAllowed` | Major / P3 | [пошук](https://github.com/MarkKmets/Project_Staynow/issues?q=is%3Aissue+BUG-024) |

Після реєстрації звітів у трекері пошукові посилання замінюються прямими посиланнями на Issues.

## Висновок про стан v1

Повний локальний юніт-набір: **155 запусків, 21 failure, 1 error, 0 skipped**. Невдалі перевірки виявляють порушення межі 30 ночей, минулої дати заїзду, вихідних ночей, округлення, семиденної межі скасування та суміжних бронювань. Це шість дефектів v1; очікування тестів відповідають SRS.

Дев’ять UI-тестів пройшли двічі. Перевірені частини ручних кейсів та відомі виключення наведено в ui-scenarios.md. Набір придатний для регресійної перевірки наступної версії.

## Джерела

[SRS 1.1](../sprint2/SRS-1.1.md), [кейси спринта 3](../sprint3/test-cases.xlsx), [статут і ротація](../team/charter.md).
