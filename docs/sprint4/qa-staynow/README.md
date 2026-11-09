# Автотести модуля бронювання StayNow

**Команда:** Backrooms. **Модуль:** staynow-module v1.0.0. **Пакет:** ua.kpi.tzyapz.staynow. **Вимоги:** SRS 1.1, BL-10…BL-70.

## Середовище і збірка

Потрібні JDK 17, Maven 3.9, Chrome та доступ до інтернету. Selenium Manager отримує драйвер автоматично.

```powershell
git clone https://github.com/MarkKmets/Project_Staynow.git
cd Project_Staynow/docs/sprint4/qa-staynow
mvn -v
mvn clean test
```

Копію можна отримати через GitHub Desktop або Code → Download ZIP для main. Після розпакування перейти до `docs/sprint4/qa-staynow`; в IntelliJ відкрити цю папку з pom.xml.

## Запуск тестів

```powershell
mvn clean test
mvn test "-DexcludedGroups=none" "-Dtest=*UiTest"
mvn test "-DexcludedGroups=none" "-Dheadless=false" "-Dtest=HomePageUiTest"
```

Перша команда запускає юніт-набір, друга — лише UI, третя — тести головної сторінки з видимим браузером. `mvn test "-DexcludedGroups=none"` запускає обидва набори; загальний BUILD FAILURE на v1 зумовлений дефектами юніт-модуля. Звіти зберігаються в target/surefire-reports/.

У main злиті внески п’яти учасників: 57 основних тест-методів і два ExampleTest. Фактичний підсумок запуску 09.11.2026: **155 запусків, 21 failure, 1 error**. UI: **9 / 0 / 0**, повторено двічі. [Протокол](docs/surefire-summary.md) містить дати, команди та список невдалих тестів.

## Структура

| Шлях | Зміст |
|---|---|
| src/main/java/ua/kpi/tzyapz/staynow/ | Незмінений модуль v1: три класи й enum |
| src/test/java/ua/kpi/tzyapz/staynow/ | Юніт-тести за класами модуля та ExampleTest |
| src/test/java/ua/kpi/tzyapz/staynow/ui/ | Дев’ять WebDriver-тестів і спільна підготовка |
| docs/coverage-matrix.md | BL → правило → тести → рецензент; зауваження review |
| docs/ui-scenarios.md | TC → кроки, локатори, очікування, тест-метод |
| docs/surefire-summary.md | Протокол Surefire і failed → BUG |
| selenium-ide/staynow.side | Smoke-набір Selenium IDE |
| selenium-ide/export/ | Два Java JUnit-експорти; довідкові чернетки поза Maven-збіркою |

## Розподіл тестів

| Учасник | Роль | Частина | Додано методів | PR із фактичним внеском |
|---|---|---|---|---|
| Кмець Марк | ТА | BL-10: дати й ночі | 9 | [PR #2](https://github.com/MarkKmets/Project_Staynow/pull/2) |
| Тіньков Данііл | КТ | BL-20 і кількість гостей | 13 | [PR #3](https://github.com/MarkKmets/Project_Staynow/pull/3) |
| Гатченко Ярослав | ІТ | BL-30, BL-40, BL-50: ціна ночі | 7 | [PR #4](https://github.com/MarkKmets/Project_Staynow/pull/4) |
| Сирота Володимир | СМ | BL-50: загальна сума | 14 | [PR #5](https://github.com/MarkKmets/Project_Staynow/pull/5) |
| Сімутін Владислав | АО | BL-60 і BL-70 | 14 | [PR #6](https://github.com/MarkKmets/Project_Staynow/pull/6) |

Рецензент — Сирота Володимир; його частину перевіряє Тіньков Данііл. Тести, які відтворюють [BUG-019](https://github.com/MarkKmets/Project_Staynow/issues/7)…024, залишаються червоними на v1. Шість звітів зареєстровані в [GitHub Issues](https://github.com/MarkKmets/Project_Staynow/issues) з міткою module.

[Досьє спринта](../README.md), [аудит](../audit.md), [таблиця задач](../task-board.md), [ретро](../retro.md).
