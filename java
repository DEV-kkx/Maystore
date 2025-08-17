document.addEventListener('DOMContentLoaded', function() {
    // Menu Mobile
    const mobileMenuBtn = document.createElement('button');
    mobileMenuBtn.innerHTML = '<i class="fas fa-bars"></i>';
    mobileMenuBtn.classList.add('mobile-menu-btn');
    document.querySelector('.main-nav .container').prepend(mobileMenuBtn);
    
    mobileMenuBtn.addEventListener('click', function() {
        document.querySelector('.main-nav ul').classList.toggle('active');
    });
    
    // Carrinho - Atualizar contador
    function updateCartCount() {
        let cart = JSON.parse(localStorage.getItem('cart')) || [];
        let totalItems = cart.reduce((total, item) => total + item.quantity, 0);
        document.querySelector('.cart-count').textContent = totalItems;
    }
    
    updateCartCount();
    
    // Adicionar ao carrinho
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            const name = this.closest('.product-card').querySelector('h3 a').textContent;
            const price = parseFloat(this.closest('.product-card').querySelector('.current-price').textContent.replace('R$ ', '').replace(',', '.'));
            const image = this.closest('.product-card').querySelector('img').src;
            
            let cart = JSON.parse(localStorage.getItem('cart')) || [];
            
            const existingItem = cart.find(item => item.id === id);
            
            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({
                    id,
                    name,
                    price,
                    image,
                    quantity: 1
                });
            }
            
            localStorage.setItem('cart', JSON.stringify(cart));
            updateCartCount();
            
            // Feedback visual
            const originalText = this.textContent;
            this.textContent = 'Adicionado!';
            this.style.backgroundColor = '#4caf50';
            
            setTimeout(() => {
                this.textContent = originalText;
                this.style.backgroundColor = '#1d3557';
            }, 1500);
        });
    });
    
    // Quick View Modal
    document.querySelectorAll('.quick-view').forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-id');
            // Aqui você faria uma requisição AJAX para buscar os detalhes do produto
            // Estou simulando com um objeto de exemplo
            const product = {
                id: productId,
                name: 'Camisa Flamengo I 2023/24',
                price: 299.90,
                oldPrice: 349.90,
                description: 'Camisa oficial do Flamengo temporada 2023/24. Material 100% poliéster com tecnologia DRY que ajuda a manter o corpo seco durante os jogos.',
                details: 'Gola redonda | Logotipo do clube e patrocinador bordados | Tecnologia de absorção de umidade',
                sizes: ['P', 'M', 'G', 'GG'],
                colors: ['Vermelho', 'Preto'],
                images: [
                    'images/camisa-flamengo-2023.jpg',
                    'images/camisa-flamengo-2023-back.jpg',
                    'images/camisa-flamengo-2023-detail.jpg'
                ]
            };
            
            const modalContent = `
                <div class="quick-view-content">
                    <div class="product-images">
                        <div class="main-image">
                            <img src="${product.images[0]}" alt="${product.name}">
                        </div>
                        <div class="thumbnails">
                            ${product.images.map(img => `
                                <img src="${img}" alt="${product.name}">
                            `).join('')}
                        </div>
                    </div>
                    <div class="product-details">
                        <h2>${product.name}</h2>
                        <div class="product-rating">
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star"></i>
                            <i class="fas fa-star-half-alt"></i>
                            <span>(24 avaliações)</span>
                        </div>
                        <div class="product-price">
                            <span class="current-price">R$ ${product.price.toFixed(2).replace('.', ',')}</span>
                            <span class="old-price">R$ ${product.oldPrice.toFixed(2).replace('.', ',')}</span>
                        </div>
                        <p class="product-description">${product.description}</p>
                        <div class="product-options">
                            <div class="option">
                                <label>Tamanho:</label>
                                <select>
                                    ${product.sizes.map(size => `
                                        <option value="${size}">${size}</option>
                                    `).join('')}
                                </select>
                            </div>
                            <div class="option">
                                <label>Cor:</label>
                                <select>
                                    ${product.colors.map(color => `
                                        <option value="${color}">${color}</option>
                                    `).join('')}
                                </select>
                            </div>
                        </div>
                        <button class="add-to-cart" data-id="${product.id}">Adicionar ao Carrinho</button>
                        <div class="product-meta">
                            <span><i class="fas fa-tag"></i> Categoria: Camisas</span>
                            <span><i class="fas fa-box"></i> Disponível em estoque</span>
                        </div>
                    </div>
                </div>
            `;
            
            document.querySelector('.modal .modal-body').innerHTML = modalContent;
            document.querySelector('.modal').style.display = 'block';
            
            // Adicionar evento ao botão de adicionar no modal
            document.querySelector('.modal .add-to-cart').addEventListener('click', function() {
                // Mesma lógica de adicionar ao carrinho
                let cart = JSON.parse(localStorage.getItem('cart')) || [];
                
                const existingItem = cart.find(item => item.id === product.id);
                
                if (existingItem) {
                    existingItem.quantity += 1;
                } else {
                    cart.push({
                        id: product.id,
                        name: product.name,
                        price: product.price,
                        image: product.images[0],
                        quantity: 1
                    });
                }
                
                localStorage.setItem('cart', JSON.stringify(cart));
                updateCartCount();
                
                // Feedback visual
                const originalText = this.textContent;
                this.textContent = 'Adicionado!';
                this.style.backgroundColor = '#4caf50';
                
                setTimeout(() => {
                    this.textContent = originalText;
                    this.style.backgroundColor = '#1d3557';
                }, 1500);
            });
        });
    });
    
    // Fechar modal
    document.querySelector('.close-modal').addEventListener('click', function() {
        document.querySelector('.modal').style.display = 'none';
    });
    
    // Fechar modal ao clicar fora
    window.addEventListener('click', function(e) {
        if (e.target === document.querySelector('.modal')) {
            document.querySelector('.modal').style.display = 'none';
        }
    });
    
    // Newsletter
    document.querySelector('.newsletter-form')?.addEventListener('submit', function(e) {
        e.preventDefault();
        const email = this.querySelector('input').value;
        
        // Simular envio
        this.innerHTML = '<p class="success-message">Obrigado por assinar nossa newsletter!</p>';
        
        // Limpar localStorage após 5 segundos (apenas para demonstração)
        setTimeout(() => {
            this.querySelector('input').value = '';
            this.innerHTML = `
                <input type="email" placeholder="Seu melhor e-mail" required>
                <button type="submit" class="btn">Assinar</button>
            `;
        }, 5000);
    });
});
