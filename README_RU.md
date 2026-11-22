# Super Pixel World — Android TV (APK-проект)

Готовый проект Android TV-приложения (WebView) с вашей игрой `index.html` внутри, поддержкой пульта Xiaomi TV (D-pad, OK, Назад), Bluetooth/USB-геймпадов и полноэкранного режима.

## Способ 1: Сборка APK без установки программ (через GitHub)
1. Создайте бесплатный репозиторий на [GitHub](https://github.com/new) и загрузите в него все файлы из этой папки (включая скрытую папку `.github`).
2. Перейдите во вкладку **Actions** → выберите **Build Android TV APK** → нажмите **Run workflow** (или сборка запустится сама после загрузки).
3. Через ~2 минуты скачайте готовый файл **`SuperPixelWorld-AndroidTV-APK`** (`app-debug.apk`) внизу страницы сборки.

## Способ 2: Сборка на компьютере в Android Studio
1. Установите **Android Studio** и откройте папку `SuperPixelWorldTV` через **File → Open**.
2. Дождитесь синхронизации Gradle.
3. В меню выберите **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. Готовый APK появится в `app/build/outputs/apk/debug/app-debug.apk`.

## Установка на телевизор Xiaomi (Android TV)
1. Скопируйте `app-debug.apk` на флешку в формате **FAT32**.
2. На телевизоре откройте **Настройки → Безопасность и ограничения** и разрешите установку из неизвестных источников для файлового менеджера.
3. Откройте флешку через **Медиаплеер** или **X-plore**, нажмите на `app-debug.apk` и установите игру.
4. Иконка **Super Pixel World** появится прямо в списке приложений на главном экране Xiaomi TV!
