function Gameboard() {
    this.winningConfigs = [
        [[0, 0], [0, 1], [0, 2]],
        [[1, 0], [1, 1], [1, 2]],
        [[2, 0], [2, 1], [2, 2]],
        [[0, 0], [1, 0], [2, 0]],
        [[0, 1], [1, 1], [2, 1]],
        [[0, 2], [1, 2], [2, 2]],
        [[0, 0], [1, 1], [2, 2]],
        [[0, 2], [1, 1], [2, 0]],
    ];

    this.newGrid = function () {
        return [
            ['.', '.', '.'],
            ['.', '.', '.'],
            ['.', '.', '.']
        ];
    }

    this.grid = this.newGrid();

    this.startNewGame = function () {
        this.grid = this.newGrid();
    }

    this.isWinner = (player) => {
        let symbol = player.symbol;

        for (let configIdx = 0; configIdx < this.winningConfigs.length; configIdx++) {
            let currentConfig = this.winningConfigs[configIdx];
            let isWinner = true;

            for (let [rowIdx, colIdx] of currentConfig) {
                if (this.grid[rowIdx][colIdx] !== symbol) {
                    isWinner = false;
                    break;
                }
            }

            if (isWinner) {
                return true;
            }
        }

        return false;
    }

    this.play = (player, rowIdx, colIdx) => {
        this.grid[rowIdx][colIdx] = player.symbol;
        return this.isWinner(player);
    }
}

function Player(symbol) {
    this.symbol = symbol;
}