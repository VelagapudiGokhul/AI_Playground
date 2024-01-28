import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static final int SIZE = 3;
    private static char playerSymbol; 
    private static char computerSymbol;
    private static boolean isComputerTurn;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter User Name");
        String name = scanner.nextLine();
        System.out.println("Welcome " + name);
        System.out.println();
        boolean playAgain = true;
        while (playAgain) {
            
            while(true){
                System.out.println("Choose your symbol: X or O");
                String symbolChoice = scanner.nextLine().toUpperCase();
                if (symbolChoice.equals("X") || symbolChoice.equals("O")) {
                    playerSymbol = symbolChoice.charAt(0);
                    computerSymbol = (playerSymbol == 'X') ? 'O' : 'X';
                    isComputerTurn = (computerSymbol == 'X');
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter X or O.");
                }
            }

            char[][] board = new char[SIZE][SIZE];
            initializeBoard(board);
            printBoard(board);
            
            if (isComputerTurn) {
                Random rand = new Random();
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                board[row][col] = computerSymbol;
                System.out.println("Computer's move:");
                printBoard(board);
                isComputerTurn = false;
            }

            while (!isGameOver(board)) {
                if (isComputerTurn) {
                    int[] computerMove = findBestMove(board);
                    board[computerMove[0]][computerMove[1]] = computerSymbol;
                    System.out.println("Computer's move:");
                    printBoard(board);
                    isComputerTurn = false;
                } else {
                    getPlayerMove(board, scanner, name);
                    printBoard(board);
                    isComputerTurn = true;
                }
            }

            if (checkWin(board, playerSymbol)) {
                System.out.println(name + " wins!");
            } else if (checkWin(board, computerSymbol)) {
                System.out.println("Computer wins!");
            } else {
                System.out.println("It's a draw!");
            }

            System.out.println("Do you want to play again? (yes/no)");
            String playAgainInput = scanner.nextLine();
            playAgain = playAgainInput.equalsIgnoreCase("yes");

            if (playAgain) {
                System.out.println("Welcome again " + name + "!");
                isComputerTurn = (computerSymbol == 'X');
            }
        }
        scanner.close();
        System.out.println("Game over!");
        System.out.println("Thanks for playing!");
    }

    private static void initializeBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private static void printBoard(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j]);
                if (j < SIZE - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i < SIZE - 1) {
                for (int k = 0; k < SIZE * 4 - 2; k++) {
                    System.out.print("-");
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void getPlayerMove(char[][] board, Scanner scanner, String name) {
        System.out.println(name + "'s move");
        int row, col;
        while (true) {
            try {
                System.out.print("Enter row (1, 2, or 3): ");
                row = scanner.nextInt() - 1;
                System.out.print("Enter column (1, 2, or 3): ");
                col = scanner.nextInt() - 1;
                scanner.nextLine(); 

                if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == ' ') {
                    board[row][col] = playerSymbol;
                    break;
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter row and column as integers.");
                scanner.nextLine();
            }
        }
    }

    private static int[] findBestMove(char[][] board) {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = computerSymbol;
                    int score = minimax(board, 0, false);
                    board[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    private static int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (isGameOver(board)) {
            return evaluate(board, depth);
        }
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = computerSymbol;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = ' ';
                        maxScore = Math.max(maxScore, score);
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = playerSymbol;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = ' ';
                        minScore = Math.min(minScore, score);
                    }
                }
            }
            return minScore;
        }
    }

    private static int evaluate(char[][] board, int depth) {
        if (checkWin(board, computerSymbol)) {
            return 10 - depth;
        } else if (checkWin(board, playerSymbol)) {
            return depth - 10;
        } else {
            return 0;
        }
    }

    public static boolean checkWin(char[][] board, char symbol) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }
        return false;
    }

    private static boolean isGameOver(char[][] board) {
        if (checkWin(board, computerSymbol) || checkWin(board, playerSymbol)) {
            return true;
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
