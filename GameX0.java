package playx0;

import java.util.Random;
import java.util.Scanner;

public class GameX0 {
    char[][] field; // матрица для игрового поля
    static int size; //количество клеток поля
    int countForWin = 3; //количество ячеек закрытых по линии
    Scanner scanner = new Scanner(System.in);
    GameX0 gameField;
    char whoseMove;//чей ход
    boolean whoToPlayWith = false;//играть друг с другом или с компьютером
    boolean gameOver = false;//проверка на окончание игры
    boolean draw = false;//проверка на ничью


    void setUpNewGame() {
        System.out.println("Начнем новую игру X0");
        this.gameField = new GameX0();// проинициализируем(создадим) gameField
        this.gameField.setSize();//проинициализируем(создадим) игровое поле
    }

    void newField() { //инициализировал поле(создал игровое поле)
        this.field = new char[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                field[row][col] = ' ';
            }
        }
        this.printField();
    }

    void setSize() {
        System.out.print("Введите размер поля, число от 3-х до 9-ти: ");
        int scanSize = this.scanner.nextInt();
        if (scanSize > 9 || scanSize < 3) {
            System.out.println("Вы ввели неверное число. Попробуйте ещё раз.");
            setSize();
        } else {
            size = scanSize;
        }
        if (size >= 5) {// при большом поле, выигрывает линия в 5 одинаковых символов
            this.countForWin = 5;
        }
        newField();
    }

    void printField() {// вывод поля на консоль
        System.out.print("  ");
        for (int i = 1; i <= size; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println();
        for (int row = 0; row < size; row++) {
            System.out.print(row + 1 + " ");
            for (int col = 0; col < size; col++) {
                System.out.print("[" + this.field[row][col] + "]");
            }
            System.out.println();
        }
    }

    void whoToPlay() {
        System.out.println("Хотите с играть с компьютером? (да/нет) ");
        String whoPlay = this.scanner.nextLine();
        if (whoPlay.equals("да")) {
            this.whoToPlayWith = true;
            System.out.println("Ок. У Вас будет \"X\"");
        } else if (whoPlay.equals("нет")) {
            this.whoToPlayWith = false;
        }
        else {
            System.out.println("Вы ввели некорректный ответ. Попробуйте ещё раз.");
            whoToPlay();
        }

    }


    void play() { // игра
        this.setUpNewGame();
        this.whoToPlay();
        int firstPlayer = new Random().nextInt(90); // случайный выбор кто первый ходит
        if (firstPlayer % 2 == 0) {
            this.whoseMove = '0';
            System.out.println("По случайному выбору, игру начинает нолик :)");
        } else {
            this.whoseMove = 'Х';
            System.out.println("По случайному выбору, игру начинает крестик :)");
        }
        while (!gameOver) {
            this.makeMove();
            this.gameOver = this.gameField.isGameOver(this.whoseMove);
            if (this.gameOver) {
                System.out.println(this.whoseMove + " Вы выиграли!!!");
            }
            this.draw = this.gameField.isDraw();// проверка на ничью
            if (this.draw && !this.gameOver) {
                System.out.println("Ничья :(");
                break;
            }
            if (this.whoseMove == 'Х') {//меняется игрок
                this.whoseMove = '0';
            } else {
                this.whoseMove = 'Х';
            }
        }
        System.out.println("Game over!");
    }


    void makeMove() {//сделать ход
        if (!whoToPlayWith) {
            playerTurn();
        } else {//игра с компьютером
            if (this.whoseMove == 'Х') {//ход человека
                this.playerTurn();
            } else {//ход компьютера
                computerMove();
            }
        }
    }

    void playerTurn() {// делает ход человек
        System.out.print(this.whoseMove + " ваш ход");
        System.out.println();
        System.out.print("Введите номер  строки (по горизонтали): ");
        int rowNum = this.scanner.nextInt();
        System.out.print("Введите номер столбца (по вертикали): ");
        int colNum = this.scanner.nextInt();
        int rowIndex1 = rowNum - 1;
        int colIndex1 = colNum - 1;
        if (this.gameField.isPlaceFree(rowIndex1, colIndex1)) {
            this.gameField.whoField(rowIndex1, colIndex1, whoseMove);
            this.gameField.printField();
        } else {
            System.out.println("Данная клеточка занята или вы вышли за границу поля. Попробуйте еще раз");
            playerTurn();
        }
    }

    void computerMove() {//делает ход компьютер
        System.out.println("\"0\" делает ход");
        int rowIndex1 = (new Random().nextInt(size));
        int colIndex1 = (new Random().nextInt(size));
        if (this.gameField.isPlaceFree(rowIndex1, colIndex1)) {
            this.gameField.whoField(rowIndex1, colIndex1, whoseMove);
            this.gameField.printField();
        } else {
            computerMove();
        }
    }

    void whoField(int rowIndex1, int colIndex1, char ch) {// меняю значение пустой ячейки
        // на значение ходящего
        this.field[rowIndex1][colIndex1] = ch;
    }

    boolean isPlaceFree(int rowIndex1, int colIndex1) {// определяет свободная ли ячейка
        if (rowIndex1 < 0 || rowIndex1 >= size || colIndex1 < 0 || colIndex1 >= size) {
            return false;
        } else return this.field[rowIndex1][colIndex1] == ' '; //true
    }

    boolean isGameOver(char player) {//крестик (проверка идет ли подряд нужное количество Х)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (checkHorizontal(row, col, player)) {
                    return true;
                } else if (checkVertical(row, col, player)) {
                    return true;
                } else if (checkCellRightDiagonal(row, col, player)) {
                    return true;
                } else if (checkCellLeftDiagonal(row, col, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkHorizontal(int row, int col, char player) {//подряд в право
        if (col > size - this.countForWin) {
            return false;
        }
        for (int i = col; i < col + this.countForWin; i++) {
            if (this.field[row][i] != player) {
                return false;
            }
        }
        return true;
    }

    boolean checkVertical(int row, int col, char player) {//подряд вниз
        if (row > size - this.countForWin) {
            return false;
        }
        for (int i = row; i < row + this.countForWin; i++) {
            if (this.field[i][col] != player) {
                return false;
            }
        }
        return true;
    }

    boolean checkCellRightDiagonal(int row, int col, char player) {//правая диагональ
        if (col > size - this.countForWin) {// строка должна быть высоко
            return false;
        }
        if (row > size - this.countForWin) {//столбец должен быть высоко
            return false;
        }
        for (int shift = 0; shift < this.countForWin; shift++) {
            int rowToCheck = row + shift;
            int colToCheck = col + shift;
            if (this.field[rowToCheck][colToCheck] != player) {
                return false;
            }
        }
        return true;
    }

    boolean checkCellLeftDiagonal(int row, int col, char player) {//левая диагональ
        if (col < this.countForWin - 1) {// строка должна быть высоко
            return false;
        }
        if (row > size - this.countForWin) {//столбец должен быть высоко
            return false;
        }
        for (int shift = 0; shift < this.countForWin; shift++) {
            int rowToCheck = row + shift;
            int colToCheck = col - shift;
            if (this.field[rowToCheck][colToCheck] != player) {
                return false;
            }
        }
        return true;
    }

    boolean isDraw() {// проверка на ничью
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.field[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

}

