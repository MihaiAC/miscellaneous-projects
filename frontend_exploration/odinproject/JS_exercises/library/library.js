const myLibrary = [];

function Book(title, author, publisher, year) {
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.year = year;
}

function addBookToLibrary(title, author, publisher, year) {
    let book = new Book(title, author, publisher, year);
    myLibrary.push(book);
}

addBookToLibrary("The Lord of the Rings", "J.R.R.Tolkien", "Allen & Unwin", 1954);
addBookToLibrary("Shogun", "James Clavell", "Hodder & Stoughton", 1975);

function displayBooks() {
    let booksListDiv = document.querySelector(".books-list");
    myLibrary.forEach(book => {
        let attributeList = document.createElement("ul");
        for (const [key, value] of Object.entries(book)) {
            let attribute = document.createElement("li");
            attribute.textContent = value.toString();
            attributeList.appendChild(attribute);
        }
        booksListDiv.appendChild(attributeList);
    });
}

displayBooks();