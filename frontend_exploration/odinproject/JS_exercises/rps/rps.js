console.log("Linked file");

RPS = ["Rock", "Paper", "Scissors"];
humanScore = 0;
computerScore = 0;

function getComputerChoice() {
    choice = Math.random()
    console.log(choice)
    if (choice < 1 / 3) {
        return RPS[0];
    }
    else if (choice < 2 / 3) {
        return RPS[1];
    }
    return RPS[2];
}

function getHumanChoice() {
    let human_choice = prompt("Input Rock | Paper | Scissors")
    if (RPS.includes(human_choice)) {
        return human_choice;
    } else {
        alert("Input discarded, refresh the page.")
    }
}

function playRound(humanChoice, computerChoice) {
    // Human wins.
    if ((humanChoice === RPS[0] && computerChoice === RPS[2]) ||
        (humanChoice === RPS[1] && computerChoice === RPS[0]) ||
        (humanChoice === RPS[2] && computerChoice === RPS[1])) {
        alert("You won this round!");
        humanScore++;
    } else if (humanChoice === computerChoice) {
        alert("You tied this round!");
    } else {
        alert("You lost this round!");
        computerScore++;
    }
}

function playGame() {
    for (let index = 0; index < 5; index++) {
        let humanChoice = getHumanChoice();
        let computerChoice = getComputerChoice();
        playRound(humanChoice, computerChoice);

        console.log(`Human score: ${humanScore}; computer score: ${computerScore}`)
    }

    if (humanScore > computerScore) {
        alert("You won!");
    } else if (humanScore === computerScore) {
        alert("You tied!");
    } else {
        alert("You lost!");
    }

    humanScore = 0;
    computerScore = 0;

}

playGame()