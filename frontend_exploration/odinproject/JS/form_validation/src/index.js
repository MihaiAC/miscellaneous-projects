import "./styles.css";
import { EmailValidator } from "./email_validation";

document.addEventListener("DOMContentLoaded", () => {
  let form = document.querySelector("form");
  console.log(form.children);
  window.emailValidator = new EmailValidator();
});
