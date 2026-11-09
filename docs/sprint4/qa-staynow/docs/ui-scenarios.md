# UI сценарії WebDriver

**Продукт:** StayNow. **Demo:** https://automationintesting.online. **Джерело:** кейси спринта 3, редакція 04.11.2026. **Тест-аналітик:** Кмець Марк. **Інженер:** Гатченко Ярослав. **Рецензент:** Сирота Володимир.

Обрано 9 повторюваних сценаріїв головної сторінки, входу адміністратора та контактної форми: п’ять High і чотири Medium. У спринті 3 частина сценаріїв бронювання була заблокована даними та BUG-010 переходу до Reservation. Обраний smoke-набір не потребує власних номерів і бронювань.

## Сценарії

| TC | Вимога | Тест-метод | Кроки (скорочено) | Локатори | Очікування | ІТ / відповідальний за PR-review |
|---|---|---|---|---|---|---|
| TC-001 | F-10 | `HomePageUiTest.homePageShowsTitleBookNowAndSixMenuItems` | відкрити `/`; прочитати заголовок, кнопку, меню | `h1`; `//a[normalize-space()='Book Now']`; `nav a.nav-link` | видимість `h1`; меню = 6 пунктів у порядку | Гатченко / Сирота |
| TC-004 | F-10 | `HomePageUiTest.adminMenuOpensLoginPage` | клік «Admin» | `//nav//a[normalize-space()='Admin']`; заголовок «Login» | URL закінчується `/admin` | Гатченко / Сирота |
| TC-034 | F-80, S-10 | `AdminLoginUiTest.validLoginOpensRoomManagement` | вхід admin/password | `input[placeholder='Enter username']`; `input[type='password']`; `//button[normalize-space()='Login']` | форма входу зникла; Logout є; URL без `password` і `?` | Гатченко / Сирота |
| TC-035 | F-80, GUI-70 | `AdminLoginUiTest.wrongPasswordKeepsLoginPage` | вхід admin/wrongpass | ті самі | видима серверна помилка; «Login» лишається; «Room #» немає | Гатченко / Сирота |
| TC-036 | GUI-70 | `AdminLoginUiTest.loginPageLayout` | відкрити `/admin`, ввести p1 | ті самі + `Front Page`, `Logout` | заголовок, підказки, `type=password`, шапка | Гатченко / Сирота |
| TC-024 | F-70, GUI-85 | `ContactFormUiTest.validFormShowsThanksWithNameAndSubject` | заповнити форму, Submit | `#name #email #phone #subject #description`; `//button[normalize-space()='Submit']` | `#name` зникло; текст містить ім’я й тему | Гатченко / Сирота |
| TC-025 | F-70 | `ContactFormUiTest.minimumLengthsAreAccepted` | Subject 5, Message 20, Phone 11 | ті самі | форму замінено подякою | Гатченко / Сирота |
| TC-027 | F-70, GUI-60 | `ContactFormUiTest.emptyFormIsNotSubmitted` | Submit без даних | ті самі + маркери помилок `.alert, .is-invalid, [role='alert']…` | форма на місці, подяки немає, помилки є | Гатченко / Сирота |
| TC-028 | F-70, GUI-60 | `ContactFormUiTest.subjectOfFourCharactersIsRejected` | Subject `Abcd` | ті самі | форма на місці, подяки немає | Гатченко / Сирота |

Усі тести мають @Tag("ui"), явні WebDriverWait, окреме створення й закриття браузера. Thread.sleep та implicit waits не використовуються. Актуальність локаторів підтверджено двома прогонами 09.11.2026: у кожному 9 запусків, 0 failures, 0 errors, 0 skipped. Підсумок двох успішних прогонів — у [протоколі Surefire](surefire-summary.md).

## Межі покриття ручних кейсів

| TC | Пріоритет у спринті 3 | Перевірена автоматизована частина | Межа покриття |
|---|---|---|---|
| TC-001 | High | Заголовок, Book Now, шість пунктів меню | Текстовий опис готелю окремим assert не перевіряється |
| TC-004 | High | Перехід Admin, точний URL /admin, заголовок Login | Основне очікування кейсу покрито |
| TC-024 | Medium | Заповнення коректних даних, видима подяка, зникнення форми, ім’я, тема, відсутність помилок | Основне очікування кейсу покрито |
| TC-025 | Medium | Нижні межі 5/20/11 і подяка з іменем | Окремої перевірки відсутності всіх повідомлень про помилку немає |
| TC-027 | Medium | Порожня форма: видима помилка, форма лишається, подяки немає | Позиція над Submit, тексти для всіх полів та довжина кожного повідомлення не перевіряються |
| TC-028 | Medium | Subject 4: видима помилка, поле лишається, подяки немає | Текст і позиція повідомлення окремо не перевіряються |
| TC-034 | High | Зникнення Login, Logout видимий, URL без пароля й параметрів | Список номерів прямо не перевіряється; Logout виключено через відомий BUG-006 |
| TC-035 | High | Сервер показав видиму помилку; Login лишився, Room # відсутній | Окрема помилкова позначка на кожному полі не перевіряється |
| TC-036 | High | Заголовок, placeholders, тип password, Login, Front Page, Logout | Підписи label Username/Password окремо не перевіряються |

Відомі дефекти спринта 3 збережені: Logout у TC-034 не автоматизовано через BUG-006; позиція блоку помилок над Submit не перевіряється через BUG-015. UI-набір охоплює перелічені поведінки, а не всі кроки ручних кейсів.
