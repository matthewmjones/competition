document.addEventListener('DOMContentLoaded', function() {
    const navbarToggle = document.querySelector('.navbar-toggle');
    const navbarMenu = document.querySelector('.navbar-menu');

    if (!navbarToggle || !navbarMenu) {
        console.warn('Navbar elements not found');
        return;
    }

    function toggleMenu() {
        const isActive = navbarMenu.classList.contains('active');
        
        navbarMenu.classList.toggle('active');
        navbarToggle.classList.toggle('active');
        navbarToggle.setAttribute('aria-expanded', !isActive);
        
        // Prevent body scroll when menu is open on mobile
        if (!isActive) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = '';
        }
    }

    function closeMenu() {
        navbarMenu.classList.remove('active');
        navbarToggle.classList.remove('active');
        navbarToggle.setAttribute('aria-expanded', 'false');
        document.body.style.overflow = '';
    }

    navbarToggle.addEventListener('click', toggleMenu);

    document.querySelectorAll('.navbar-nav a').forEach(link => {
        link.addEventListener('click', closeMenu);
    });

    document.addEventListener('click', (e) => {
        if (!navbarToggle.contains(e.target) && !navbarMenu.contains(e.target)) {
            closeMenu();
        }
    });

    navbarToggle.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            toggleMenu();
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && navbarMenu.classList.contains('active')) {
            closeMenu();
            navbarToggle.focus();
        }
    });

    window.addEventListener('resize', () => {
        if (navbarMenu.classList.contains('active')) {
            closeMenu();
        }
    });

    function setActiveLink() {
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll('.navbar-nav a');
        
        navLinks.forEach(link => {
            link.classList.remove('active');
            
            const linkPath = link.getAttribute('href');
            
            if (currentPath === linkPath) {
                link.classList.add('active');
            } else if (currentPath === '/' && linkPath === '/') {
                link.classList.add('active');
            } else if (currentPath.startsWith('/dashboard/company/') && linkPath === '/dashboard/company') {
                // Skip - don't highlight "Select Company" when viewing specific company
            } else if (currentPath.startsWith('/dashboard') && linkPath === '/dashboard' && currentPath !== '/dashboard/company') {
                link.classList.add('active');
            }
        });
    }

    setActiveLink();
});