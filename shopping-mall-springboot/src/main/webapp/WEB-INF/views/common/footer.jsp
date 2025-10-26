<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<style>
    /* --- 푸터 스타일 --- */
    .footer {
        background-color: #f8f9fa;
        padding: 50px 0;
        text-align: center;
        color: #6c757d;
        font-size: 14px;
        margin-top: auto;
    }

    .footer-content {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 24px;
    }

    .footer-nav {
        display: flex;
        justify-content: center;
        gap: 30px;
        margin-bottom: 20px;
        flex-wrap: wrap;
    }

    .footer-nav a {
        color: #6c757d;
        transition: color 0.2s ease;
    }

    .footer-nav a:hover {
        color: var(--color-text, #212529);
    }

    .footer-socials {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-bottom: 20px;
        font-size: 20px;
    }

    .footer-socials a {
        color: #6c757d;
        transition: all 0.2s ease;
        cursor: pointer;
    }

    .footer-socials a:hover {
        color: var(--color-primary, #667eea);
        transform: scale(1.1);
    }

    .footer-copyright {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid #dee2e6;
    }

    @media (max-width: 768px) {
        .footer {
            padding: 30px 0;
        }

        .footer-nav {
            flex-direction: column;
            gap: 15px;
        }
    }
</style>

<footer class="footer">
    <div class="footer-content">
        <div class="footer-nav">
            <a href="#" data-action="about">About</a>
            <a href="#" data-action="contact">Contact</a>
            <a href="#" data-action="privacy">Privacy Policy</a>
            <a href="#" data-action="terms">Terms of Service</a>
        </div>
        
        <div class="footer-socials">
            <a href="#" data-social="facebook" title="Facebook">📘</a>
            <a href="#" data-social="instagram" title="Instagram">📷</a>
            <a href="#" data-social="twitter" title="Twitter">🐦</a>
            <a href="#" data-social="youtube" title="YouTube">📺</a>
        </div>
        
        <div class="footer-copyright">
            <p>&copy; 2025 MVC Shop. All rights reserved.</p>
        </div>
    </div>
</footer>