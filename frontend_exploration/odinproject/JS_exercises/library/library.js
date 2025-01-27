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

var CURRENT_ID = 0;

function Book(title, author, publisher, year) {
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.year = year;

    this.id = "Book " + CURRENT_ID.toString();
    CURRENT_ID += 1;

    this.read = false;
}

Book.prototype.PUBLIC_ATTRS = ['title', 'author', 'publisher', 'year']

function deleteBook(book_id) {
    let ulElement = document.getElementById(book_id);
    booksListDiv.removeChild(ulElement);

    const bookIndex = myLibrary.findIndex(book => book.id === book_id);
    if (bookIndex !== -1) {
        myLibrary.splice(bookIndex, 1);
    }
}

function toggleReadBook(book_id) {
    const bookIndex = myLibrary.findIndex(book => book.id === book_id);
    if (bookIndex !== -1) {
        if (myLibrary[bookIndex].read) {
            myLibrary[bookIndex].read = false;
        } else {
            myLibrary[bookIndex].read = true;
        }
    }
}

function displayBook(book) {
    let attributeList = document.createElement("ul");
    attributeList.setAttribute("id", book.id);
    book.PUBLIC_ATTRS.forEach(attr => {
        let listElem = document.createElement("li");
        listElem.textContent = book[attr].toString();
        attributeList.appendChild(listElem);
    })

    // Add read checkbox
    let checkBox = document.createElement("input");
    checkBox.type = "checkbox";
    checkBox.checked = false;
    checkBox.addEventListener("click", (e) => {
        toggleReadBook(book.id);
        if (checkBox.checked) {
            attributeList.style.backgroundColor = "rgb(203, 255, 120)";
        } else {
            attributeList.style.backgroundColor = "rgb(238, 238, 238)";
        }
    })
    let label = document.createElement("label");
    label.textContent = "Read";
    label.appendChild(checkBox);
    attributeList.appendChild(label);

    // Add delete button
    let deleteBtn = document.createElement("button");
    deleteBtn.textContent = 'Delete book';
    deleteBtn.setAttribute("class", "delete-btn");
    deleteBtn.addEventListener("click", (e) => {
        deleteBook(book.id);
    })
    attributeList.appendChild(deleteBtn);



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

