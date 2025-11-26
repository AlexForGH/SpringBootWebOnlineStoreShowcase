document.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('toast');

    if (toast && toast.textContent.trim() !== '') {
        // Показываем элемент
        toast.style.display = 'block';

        // Базовые стили (левый верхний угол)
        toast.style.cssText += `
            position: fixed;
            top: 40px;
            left: 40px;
            padding: 20px 30px;
            min-width: 300px;
            max-width: 450px;
            border-radius: 12px;
            color: #333;
            font-family: Arial, sans-serif;
            font-size: 16px;
            line-height: 1.5;
            text-align: left;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
            z-index: 9999;
            opacity: 1;
            transition: opacity 0.4s ease-out, transform 0.4s ease-out;
            background: linear-gradient(135deg,
                ${toast.dataset.toastType === 'success' ? '#d4edda' : '#f8d7da'} 0%,
                ${toast.dataset.toastType === 'success' ? '#c3e6cb' : '#f1b8c4'} 100%);
        `;

        // Кнопка закрытия (в правом верхнем углу тоста)
        const closeBtn = document.createElement('button');
        closeBtn.innerHTML = '&times;';
        closeBtn.style.position = 'absolute';
        closeBtn.style.top = '12px';
        closeBtn.style.right = '12px';
        closeBtn.style.background = 'none';
        closeBtn.style.border = 'none';
        closeBtn.style.fontSize = '20px';
        closeBtn.style.fontWeight = 'bold';
        closeBtn.style.color = '#666';
        closeBtn.style.cursor = 'pointer';
        closeBtn.style.padding = '0';
        closeBtn.addEventListener('click', () => toast.remove());
        toast.appendChild(closeBtn);


        // Автоматическое скрытие через 4 секунды
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(-10px)';
            setTimeout(() => toast.remove(), 400);
        }, 4000);
    }
});