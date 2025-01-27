const myLibrary = [];
const booksListDiv = document.querySelector(".books-list");
const showDialogBtn = document.getElementById("show-dialog");
const closeDialogBtn = document.getElementById("js-close");
const dialog = document.getElementById("dialog");

// Form inputs
const titleInput = document.getElementById("title");
const authorInput = document.getElementById("author");
const publisherInput = document.getElementById("publisher");
const yearInput = document.getElementById("year");

function Book(title, author, publisher, year) {
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.year = year;
}

function displayBook(book) {
    let attributeList = document.createElement("ul");
    for (const [key, value] of Object.entries(book)) {
        let attribute = document.createElement("li");
        attribute.textContent = value.toString();
        attributeList.appendChild(attribute);
    }
    booksListDiv.appendChild(attributeList);
}

function addBookToLibrary(title, author, publisher, year) {
    let book = new Book(title, author, publisher, year);
    myLibrary.push(book);
    displayBook(book);
}

function readInput() {
    title = titleInput.value;
    author = authorInput.value;
    publisher = publisherInput.value;
    year = yearInput.value;

    return [title, author, publisher, year]
}

function isValidTextInput(text_input) {
    return text_input !== null && text_input.trim().length > 0;
}

function isValidYear(year_str) {
    const year_num = Number(year_str);
    return (!Number.isNaN(year_num)) && year_num >= 1700 && year_num <= 2025;
}

// Should use input validators instead, but it's a toy example.
function validateInput(input) {
    for (let index = 0; index < input.length - 1; index++) {
        if (!isValidTextInput(input[index])) {
            return false;
        }
    }
    return isValidYear(input[3]);
}

function flushInput() {
    titleInput.value = "";
    authorInput.value = "";
    publisherInput.value = "";
    yearInput.value = "";
}

showDialogBtn.addEventListener("click", () => {
    dialog.showModal();
});

closeDialogBtn.addEventListener("click", (e) => {
    e.preventDefault();
    input = readInput();
    if (validateInput(input)) {
        addBookToLibrary(...input);
        flushInput(input);
    }

    dialog.close();
})


addBookToLibrary("The Lord of the Rings", "J.R.R.Tolkien", "Allen & Unwin", 1954);
addBookToLibrary("Shogun", "James Clavell", "Hodder & Stoughton", 1975);
displayBooks();
