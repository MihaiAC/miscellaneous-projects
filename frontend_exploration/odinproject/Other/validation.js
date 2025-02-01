var formElem = document.querySelector("form");
var password = document.getElementById("password");
var confirmPassword = document.getElementById("confirmPassword");

function validatePassword() {
    if (password.value !== confirmPassword.value) {
        confirmPassword.setCustomValidity("Passwords do not match!");
    } else {
        confirmPassword.setCustomValidity('');
    }
}

formElem.addEventListener("submit", function (event) {

})

password.onchange = validatePassword;
confirmPassword.onchange = validatePassword;