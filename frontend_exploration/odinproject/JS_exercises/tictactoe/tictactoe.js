const gameboard = (function () {
    winningConfigs = [
        [[0, 0], [0, 1], [0, 2]],
        [[1, 0], [1, 1], [1, 2]],
        [[2, 0], [2, 1], [2, 2]],
        [[0, 0], [1, 0], [2, 0]],
        [[0, 1], [1, 1], [2, 1]],
        [[0, 2], [1, 2], [2, 2]],
        [[0, 0], [1, 1], [2, 2]],
        [[0, 2], [1, 1], [2, 0]],
    ];

    const newGrid = function () {
        return [
            ['.', '.', '.'],
            ['.', '.', '.'],
            ['.', '.', '.']
        ];
    }

    let grid = newGrid();

    const startNewGame = function () {
        grid = newGrid();
    }

    const isWinner = (player) => {
        let symbol = player.symbol;

        for (let configIdx = 0; configIdx < winningConfigs.length; configIdx++) {
            let currentConfig = winningConfigs[configIdx];
            let isWinner = true;

            for (let [rowIdx, colIdx] of currentConfig) {
                if (grid[rowIdx][colIdx] !== symbol) {
                    isWinner = false;
                    break;
                }
            }

            if (isWinner) {
                console.log(configIdx);
                return true;
            }
        }

        return false;
    }

    const play = (player, rowIdx, colIdx) => {
        grid[rowIdx][colIdx] = player.symbol;
        return isWinner(player);
    }

    return { play, startNewGame };
})();

const game = (function () {
    // Initialise board (on page load, once).
    const htmlBoard = document.getElementById("tictactoe");
    var boardButtons = [];
    let player1 = null;
    let player2 = null;
    let currentPlayer = null;
    let isGameFinished = false;
    let nElementsSet = 0;


    const initialiseGame = function (initPlayer1, initPlayer2) {
        player1 = initPlayer1;
        player2 = initPlayer2;
        currentPlayer = player1;
    }

    const resetGame = function () {
        [player1, player2, currentPlayer] = [null, null, null];
        gameboard.startNewGame();
        boardButtons.forEach(btn => {
            btn.textContent = "";
        });
        isGameFinished = false;
    }

    // Make valid move and return result.
    const makeMove = function (rowIdx, colIdx) {
        let result = gameboard.play(currentPlayer, rowIdx, colIdx);

        if (result) {
            isGameFinished = true;
            if (currentPlayer === player1) {
                alert("Player 1 won!");
            } else {
                alert("Player 2 won!");
            }
        } else {
            nElementsSet += 1;
            if (nElementsSet === 9) {
                isGameFinished = true;
                alert("You tied!");
            }
            return true;
        }
    }

    // Called only if move was valid.
    const swapPlayers = function () {
        if (currentPlayer === player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    for (let rowIdx = 0; rowIdx < 3; rowIdx++) {
        for (let colIdx = 0; colIdx < 3; colIdx++) {
            let newBtn = document.createElement('div');
            newBtn.classList.add(`cell`);
            newBtn.classList.add(`${rowIdx}${colIdx}`);
            newBtn.addEventListener('click', (e) => {
                if (newBtn.textContent.trim() === "" && (!isGameFinished)) {
                    newBtn.textContent = currentPlayer.symbol;
                    makeMove(rowIdx, colIdx);
                    swapPlayers();
                }
            })
            boardButtons.push(newBtn);
            htmlBoard.appendChild(newBtn);
        }
    }

    let resetBtn = document.createElement('button');
    resetBtn.textContent = "Reset";
    resetBtn.addEventListener('click', () => resetGame());
    resetBtn.classList.add("reset-btn");
    htmlBoard.appendChild(resetBtn);

    return { initialiseGame };

})();

function Player(symbol) {
    this.symbol = symbol;
}

p1 = new Player("X");
p2 = new Player("O");

game.initialiseGame(p1, p2);
