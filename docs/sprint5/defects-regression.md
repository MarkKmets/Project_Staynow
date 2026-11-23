# Дефекти регресії StayNow

**Команда:** Backrooms. **Спринт:** 5. **Дата документа:** 20.11.2026. **Відповідальний:** Кмець Марк.

## Реєстр після регресії

До метрик включено 23 підтверджені дефекти: 18 відкритих і 5 закритих. Серед відкритих 2 Reopened і 4 New. Кожен дефект має одну основну вимогу.

| ID | Основна вимога | Severity | Priority | Статус | Дата ретесту |
|---|---|---|---|---|---|
| BUG-002 | F-30 | Normal | P3 | Open | 17.11.2026 |
| BUG-003 | F-30 | Normal | P3 | Open | 19.11.2026 |
| BUG-006 | F-85 | Minor | P3 | Open | 19.11.2026 |
| BUG-007 | S-10 | Critical | P3 | Closed | 19.11.2026 |
| BUG-008 | F-90 | Major | P2 | Open | 17.11.2026 |
| BUG-009 | F-90 | Major | P3 | Open | 17.11.2026 |
| BUG-010 | F-20 | Major | P2 | Open | 19.11.2026 |
| BUG-011 | F-85 | Major | P3 | Open | 19.11.2026 |
| BUG-014 | F-100 | Major | P2 | Open | 17.11.2026 |
| BUG-015 | F-70 | Minor | P3 | Open | 17.11.2026 |
| BUG-016 | F-10 | Minor | P2 | Open | 17.11.2026 |
| BUG-017 | F-90 | Minor | P2 | Open | 17.11.2026 |
| BUG-018 | F-100 | Major | P2 | Open | 17.11.2026 |
| BUG-019 | BL-10 | Major | P3 | Closed | 17.11.2026 |
| BUG-020 | BL-10 | Major | P3 | Reopened | 17.11.2026 |
| BUG-021 | BL-30 | Major | P3 | Closed | 17.11.2026 |
| BUG-022 | BL-50 | Minor | P3 | Closed | 17.11.2026 |
| BUG-023 | BL-60 | Major | P3 | Reopened | 17.11.2026 |
| BUG-024 | BL-70 | Major | P3 | Closed | 17.11.2026 |
| BUG-025 | BL-40 | Major | P3 | New | 17.11.2026 |
| BUG-026 | F-10 | Minor | P3 | New | 17.11.2026 |
| BUG-027 | GUI-20 | Minor | P3 | New | 17.11.2026 |
| BUG-028 | GUI-70 | Minor | P3 | New | 19.11.2026 |

Статуси дефектів модуля підтверджені коментарями [GitHub Issues #7–13](https://github.com/MarkKmets/Project_Staynow/issues). Статуси демостенду зведені тут за результатами аркуша «Верифікація» у [журналі](regression-log.xlsx). При порівнянні збережено вихідні статуси спринту 3.

## BUG-007

**Вимога:** S-10. **Severity / Priority:** Critical / P3. **Статус:** Closed (not reproducible).

Ретест 19.11.2026: після Logout і закриття вікон Incognito адресу Messages відкрито в новій приватній сесії. Система перенаправила на /admin із Login; повідомлення недоступні. Дефект закрито у реєстрі спринту 5 20.11.2026 з причиною невідтворюваності. Базовий звіт спринту 3 збережено без змін.

Доступ до приватних даних після повернення кнопкою Назад у TC-037 обліковується окремо як BUG-011. Повний TC-038 має Failed через перехід Logout на головну сторінку, BUG-006.

## Дефекти модуля v1

| ID | Результат v2 | Статус |
|---|---|---|
| BUG-019 | 30 ночей: порожній список помилок. | Closed |
| BUG-020 | Заїзд вчора, виїзд завтра: [] замість [CHECK_IN_IN_PAST]. | Reopened |
| BUG-021 | П’ятницю та суботу визнано вихідними. Надбавка до неділі не застосовується. | Closed |
| BUG-022 | HALF_UP підтверджено на 129.987 → 129.99 та 0.195 → 0.20. | Closed |
| BUG-023 | Плата 500.00 замість 0.00, повернення 500.00 замість 1000.00. | Reopened |
| BUG-024 | Суміжні інтервали доступні. Друге суміжне бронювання створено. | Closed |

<a id="bug-025"></a>
## BUG-025: регресія v2 — завищена ціна ночі в листопаді

**Severity / Priority** — Major / P3  
**Вимога** — SRS 1.1, BL-40. Наслідок проявляється в розрахунку за BL-50.  
**Модуль** — StayNow v2.0.0  
**Дата відтворення** — 17.11.2026, Windows, JUnit 5, Maven Surefire 3.2.5.

### Кроки відтворення

1. Запустити `mvn clean test` у Maven-проєктах v1.0.0 і v2.0.0 з тим самим набором тестів спринту 4.
2. У `PriceCalculatorTest` порівняти результати `seasonFactor[7]`, `[8]`, `[29]`, `[30]` для листопада та `nightPrice[3]` для середи 04.11.2026 з базовим тарифом 100.00.

### Очікуваний результат

У листопаді діє коефіцієнт низького сезону 0.80. Ціна ночі 04.11.2026 становить 80.00.

### Фактичний результат

- **v1.0.0** — коефіцієнт 0.80, ціна 80.00. Усі п’ять перевірок пройшли.
- **v2.0.0** — коефіцієнт 1.00, ціна 100.00. Усі п’ять перевірок упали.

У v2 листопад відсутній у `LOW_SEASON_MONTHS`. У наведеному прикладі ціна ночі завищена на 20.00, або на 25% від очікуваної.

### Докази

- [Вихідний тест PriceCalculatorTest](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint4/qa-staynow/src/test/java/ua/kpi/tzyapz/staynow/PriceCalculatorTest.java)
- [Звіт Surefire v1](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint5/surefire-v1/TEST-ua.kpi.tzyapz.staynow.PriceCalculatorTest.xml)
- [Звіт Surefire v2](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint5/surefire-v2/TEST-ua.kpi.tzyapz.staynow.PriceCalculatorTest.xml)

**Повний звіт і докази:** https://github.com/MarkKmets/Project_Staynow/issues/13

<a id="bug-026"></a>
## BUG-026: регресія v2 — Book Now прокручує сторінку так, що форма бронювання відображається не повністю

**Severity / Priority** — Minor / P3
**Вимога** — SRS 1.1, F-10.
**Тест-кейс** — TC-003.
**Об’єкт** — поточний демостенд automationintesting.online.
**Дата відтворення** — 17.11.2026.
**Середовище** — Windows 11, Chrome Incognito, розгорнуте вікно.

### Порівняння до/після
Спринт 3: TC-003 — Passed за записом Run 1 від 28.10.2026.
Спринт 5: TC-003 — Failed від 17.11.2026.

Після натискання Book Now заголовок форми опиняється вище видимої області, а верхнє меню частково перекриває поля дат.

### Кроки відтворення

1. Відкрити нове вікно Chrome Incognito.
2. Перейти на https://automationintesting.online/.
3. Натиснути верхню кнопку Book Now.
4. Дочекатися завершення прокручування.

### Очікуваний результат

У видимій області показано форму Check Availability & Book Your Stay:
заголовок, поля Check In і Check Out та кнопку Check Availability.

### Фактичний результат

Адреса змінюється на /#booking. Сторінка прокручується занадто далеко:
заголовок форми опиняється вище видимої області, а верхнє меню
частково перекриває поля дат. Кнопка Check Availability залишається видимою.

### Джерела порівняння

- [Тест-кейс TC-003 — каталог спринту 3](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/test-cases.xlsx)
- [Базовий результат TC-003 — Run 1 від 28.10.2026](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/execution-log.xlsx)

### Вплив

Перехід до бронювання показує форму не повністю.
Для перегляду її заголовка й полів потрібно вручну прокрутити сторінку вгору.
TC-003 завершується Failed.

<img width="1679" height="1001" alt="image" src="https://github.com/user-attachments/assets/49504291-ced6-4d1e-804a-432751ceffd8" />

**Повний звіт і докази:** https://github.com/MarkKmets/Project_Staynow/issues/14

<a id="bug-027"></a>
## BUG-027: регресія v2 — поля Check In і Check Out показують дд/мм/рррр для en-US

**Severity / Priority** — Minor / P3
**Вимога** — SRS 1.1, GUI-20.
**Тест-кейс** — TC-009.
**Об’єкт** — поточний демостенд automationintesting.online.
**Дата відтворення** — 17.11.2026.
**Середовище** — Windows 11, Google Chrome, Incognito.
**Передумова** — English (United States) встановлено першою бажаною мовою; Chrome перезапущено перед перевіркою.

### Порівняння до/після

**Спринт 3:** TC-009 — Passed за записом Run 1 від 28.10.2026.

**Спринт 5:** TC-009 — Failed від 17.11.2026.
Для en-US поля показують 21/12/2026 і 24/12/2026 замість очікуваних 12/21/2026 і 12/24/2026.

### Кроки відтворення

1. У звичайному вікні Chrome відкрити chrome://settings/languages.
2. Перемістити English (United States) на першу позицію у Preferred languages.
3. Перезапустити Chrome.
4. Відкрити нове вікно Incognito.
5. Перейти на https://automationintesting.online/.
6. Натиснути Booking.
7. У календарі Check In вибрати 21 грудня 2026.
8. У календарі Check Out вибрати 24 грудня 2026.

### Очікуваний результат

Відповідно до GUI-20 для мови браузера en-US:
- Check In показує 12/21/2026;
- Check Out показує 12/24/2026.

### Фактичний результат

Поля зберігають формат дд/мм/рррр:
- Check In показує 21/12/2026;
- Check Out показує 24/12/2026.

TC-009 завершується з Failed.

### Джерела порівняння

- [Тест-кейс TC-009 — каталог спринту 3](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/test-cases.xlsx)
- [Базовий результат TC-009 — Run 1 від 28.10.2026](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/execution-log.xlsx)

### Вплив

Формат дат не відповідає налаштуванню мови браузера та вимозі GUI-20.

### Докази

1. Скриншот Preferred languages: English (United States) стоїть першою.
2. Скриншот Check In і Check Out із фактичними значеннями дат.

<img width="1679" height="437" alt="image" src="https://github.com/user-attachments/assets/9506d064-9cd3-4a6c-816e-3602f1c46a0c" />

<img width="1679" height="998" alt="image" src="https://github.com/user-attachments/assets/27b47f7d-f25b-4665-baa0-c3b092f77643" />

**Повний звіт і докази:** https://github.com/MarkKmets/Project_Staynow/issues/15

<a id="bug-028"></a>
## BUG-028: регресія v2 — поля входу не позначаються як помилкові після неправильного пароля

**Severity / Priority:** Minor / P3.
**Основна вимога:** GUI-70.
**Пов’язана вимога:** F-80.
**Тест-кейс:** TC-035.
**Об’єкт:** поточний демостенд automationintesting.online.
**Дата перевірки:** 19.11.2026.
**Середовище:** Windows 11, Chrome Incognito.

### Кроки відтворення

1. У новому вікні Chrome Incognito відкрити https://automationintesting.online/admin.
2. У Username ввести `admin`.
3. У Password ввести `wrongpass`.
4. Натиснути Login.

### Очікуваний результат

Система залишається на сторінці входу, доступ до керування номерами не надається. Поля форми входу позначені як помилкові відповідно до GUI-70.

### Фактичний результат

Система залишається на сторінці входу та показує червоний банер «Invalid credentials». Доступ до керування номерами не надано.

Водночас поля Username і Password не мають видимого позначення помилки: їхні рамки залишаються звичайними.

### Джерела порівняння

- [Тест-кейс TC-035 — каталог спринту 3](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/test-cases.xlsx)
- [Базовий результат TC-035 — Run 1 від 28.10.2026](https://github.com/MarkKmets/Project_Staynow/blob/main/docs/sprint3/execution-log.xlsx)

### Вплив

Не виконано вимогу GUI-70 щодо позначення помилкових полів. Відмова у вході працює правильно.

### Порівняння результатів

У журналі спринту 3 від 28.10.2026 TC-035 має статус Passed.
Під час повторної перевірки 19.11.2026 — Failed.

Попередній запис підтверджує відмову у вході, але не містить окремого доказу позначення полів. Тому момент появи цього відхилення не встановлено.

### Доказ

Скриншот після натискання Login із повідомленням «Invalid credentials».

<img width="1679" height="1003" alt="image" src="https://github.com/user-attachments/assets/daafe902-5d67-4375-a573-c4377cd59352" />

**Повний звіт і докази:** https://github.com/MarkKmets/Project_Staynow/issues/16

## Порівняння та межі обліку

BUG-025 виявлено у п’яти виконаннях JUnit, які на v1 проходили. BUG-026 і BUG-027 пов’язані з переходами TC-003 і TC-009 із Passed у Failed. Для BUG-028 журнал фіксує TC-035 Passed → Failed; базовий запис описує відмову у вході, а стан полів окремо не описаний. Цю особливість бази збережено у звіті.

BUG-001 і BUG-004 відхилені через відсутність вимоги до конкретного тарифу. BUG-005 має історичний статус Cannot Reproduce. BUG-012 і BUG-013 збережені як дослідницькі спостереження. Ці п’ять записів не входять до метрик 23 підтверджених дефектів.

**Джерела:** [журнал](regression-log.xlsx), [матриця](traceability-matrix.xlsx), [Surefire v1](surefire-v1/), [Surefire v2](surefire-v2/), [базові звіти](../sprint3/bug-reports.md).
