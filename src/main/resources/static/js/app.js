document.addEventListener('DOMContentLoaded', function() {
    // 1. Сохраняем позицию перед отправкой формы или обновлением
    window.addEventListener('beforeunload', function() {
        sessionStorage.setItem('scrollPosition', window.scrollY);
    });

    // 2. Восстанавливаем позицию ПОСЛЕ полной загрузки страницы
    window.addEventListener('load', function() {
        const savedPosition = sessionStorage.getItem('scrollPosition');

        if (savedPosition) {
            // Плавный скролл к сохранённой позиции
            window.scrollTo({
                top: parseInt(savedPosition),
                left: 0,
                behavior: 'smooth'  // Плавный переход
            });

            // Очищаем хранилище после успешного восстановления
            sessionStorage.removeItem('scrollPosition');
        }
    });

    // 3. Дополнительная защита: обработка навигации назад/вперёд
    window.addEventListener('popstate', function() {
        const savedPosition = sessionStorage.getItem('scrollPosition');
        if (savedPosition) {
            window.scrollTo(0, parseInt(savedPosition));
            sessionStorage.removeItem('scrollPosition');
        }
    });
});