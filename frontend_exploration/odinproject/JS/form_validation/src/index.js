import "./styles.css";
import { EmailValidator } from "./email_validation";
import { PostcodeValidator } from "./postcode_validation";

document.addEventListener("DOMContentLoaded", () => {
  window.emailValidator = new EmailValidator();
  window.postcodeValidator = new PostcodeValidator();
});
