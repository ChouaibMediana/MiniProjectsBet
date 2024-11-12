// Variables globales pour stocker les produits
let products = [];

// Lancer l'application quand le DOM est chargé
document.addEventListener('DOMContentLoaded', function() {
    getProducts();
    document.getElementById('grid').addEventListener('click', setGridView);
    document.getElementById('list').addEventListener('click', setListView);
});

// Récupérer les données des produits depuis l'API
function getProducts() {
    fetch('https://fake-coffee-api.vercel.app/api')
        .then(response => response.json())
        .then(data => {
            products = data; // Stocker les produits
            displayProducts(products); // Afficher les produits
        })
        .catch(error => console.error('Erreur lors de la récupération des produits:', error));
}

// Créer une carte de produit
function createProductCard(product) {
    const card = document.createElement('div');
    card.classList.add('product-card');

    card.innerHTML = `
        <img src="${product.image_url}" alt="${product.name}">
        <h3>${product.name}</h3>
        <div class="product-info">
            <h4 class="price">${product.price} dh</h4>
            <p class="description">${product.description}</p>
            <button class="add-to-cart">+</button>
        </div>
    `;

    return card;
}

// Afficher les produits
function displayProducts(products) {
    const container = document.querySelector('.product-content');

    // Vider le contenu du conteneur
    container.innerHTML = '';

    // Ajouter chaque produit
    products.forEach(product => {
        const productCard = createProductCard(product);
        container.appendChild(productCard);
    });
}

// Fonction pour passer en vue grille
function setGridView() {
    document.querySelectorAll('.product-card').forEach(card => {
        card.style.width = '200px'; // Vue grille : ajuster la largeur
        card.style.display = 'block';
    });
}

// Fonction pour passer en vue liste
function setListView() {
    document.querySelectorAll('.product-card').forEach(card => {
        card.style.width = '100%'; // Vue liste : ajuster la largeur
        card.style.display = 'flex';
        card.style.alignItems = 'center';
    });

    document.querySelectorAll('.product-card img').forEach(img => {
        img.style.maxWidth = '200px'; // Limiter la taille de l'image en vue liste
    });

    document.querySelectorAll('.product-card button').forEach(btn => {
        btn.style.alignSelf = 'flex-end';
    });
}

// Filtrer les produits par recherche
function filterProducts() {
    const searchTerm = document.getElementById('search-input').value.toLowerCase();
    const filteredProducts = products.filter(product => 
        product.name.toLowerCase().includes(searchTerm)
    );
    
    displayProducts(filteredProducts); // Afficher les produits filtrés
}

// Écouteur d'événement pour le champ de recherche
document.getElementById('search-input').addEventListener('input', filterProducts);
