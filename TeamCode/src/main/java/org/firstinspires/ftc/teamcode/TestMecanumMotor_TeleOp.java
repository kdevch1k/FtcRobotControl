// Основной класс для управления роботом в TeleOp режиме
package org.firstinspires.ftc.teamcode;

// Использую TeleOp (базовый OpMode), а не LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="TEST_OP_MODE")
public class TestMecanumMotor_TeleOp extends OpMode {

    private DcMotor flMotor;
    private DcMotor frMotor;
    private DcMotor blMotor;
    private DcMotor brMotor;

    // Настройка управления
    // 0 - левый Y, 1 - левый X, 2 - правый Y, 3 - правый X
    // Вперед/Назад
    // По умолчанию: Левый стик Y, стандартно
    private final int axialAxisID = 0;
    private final double axialDirection = 1.0;

    private final int lateralAxisID = 1;
    private final double lateralDirection = -1.0;

    private final int yawAxisID = 3;
    private final double yawDirection = -1.0;


    // Метод, который сопоставляет ID из конфига с фактической осью геймпада
    private double getGamepadAxis(int axisID) {
        switch (axisID) {
            case 0: return gamepad1.left_stick_y;
            case 1: return gamepad1.left_stick_x;
            case 2: return gamepad1.right_stick_y;
            case 3: return gamepad1.right_stick_x;
            default:
                return 0.0;
        }
    }

    // Инициализация
    // Вызывается лишь раз при нажатия кнопки Init на DH
    @Override
    public void init() {
        // Сопоставление моторов с HardwareMap
        flMotor = hardwareMap.get(DcMotor.class, "front_left_motor");
        frMotor = hardwareMap.get(DcMotor.class, "front_right_motor");
        blMotor = hardwareMap.get(DcMotor.class, "back_left_motor");
        brMotor = hardwareMap.get(DcMotor.class, "back_right_motor");

        // Настройка направления моторов
        flMotor.setDirection(DcMotor.Direction.REVERSE);
        blMotor.setDirection(DcMotor.Direction.REVERSE);
        frMotor.setDirection(DcMotor.Direction.FORWARD);
        brMotor.setDirection(DcMotor.Direction.FORWARD);

        // Установка режима работы без энкодеров (они понадобятся в автоматике но потом)
        flMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Вывод
        telemetry.addData("Status", "Initialized");
    }

    // Работает анологичко, вызывается только после нажатия start и вызывается один раз
    @Override
    public void start() {
        // Все, что мы делаем, лишь обнуляем моторы, хотя вопрос а нужны ли эти строки, ну и хуй с ними
        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        blMotor.setPower(0.0);
        brMotor.setPower(0.0);

        telemetry.addData("Status", "Running");
    }

    // А вот это уже основная функция, то же, что и в ардуино
    // Вызывается многократно, в теории быстрее чем в Lineral
    @Override
    public void loop() {
        // Логика управления Mecanum

        // Считывание геймпада
        double axial = getGamepadAxis(axialAxisID) * axialDirection;
        double lateral = getGamepadAxis(lateralAxisID) * lateralDirection;
        double yaw = getGamepadAxis(yawAxisID) * yawDirection;

        // Математика Mecanum
        double flPower = axial + lateral + yaw;
        double frPower = axial - lateral - yaw;
        double blPower = axial - lateral + yaw;
        double brPower = axial + lateral - yaw;

        // Нормализация мощности
        double max;
        max = Math.max(Math.abs(flPower), Math.abs(frPower));
        max = Math.max(max, Math.abs(blPower));
        max = Math.max(max, Math.abs(brPower));

        if (max > 1.0) {
            flPower /= max;
            frPower /= max;
            blPower /= max;
            brPower /= max;
        }

        // Установка мощности моторам
        flMotor.setPower(flPower);
        frMotor.setPower(frPower);
        blMotor.setPower(blPower);
        brMotor.setPower(brPower);

        // Телеметрия

        // Обновление данных в цикле loop()
        telemetry.addData("Status", "TeleOp Active");
        telemetry.addData("flPower", "%.2f", flPower);
        telemetry.addData("frPower", "%.2f", frPower);
        telemetry.addData("blPower", "%.2f", blPower);
        telemetry.addData("brPower", "%.2f", brPower);

        // Вообще у меня вопрос нужен ли тут вызов апдейта, вроде тут он сам вызывается, но пусть будет
        telemetry.update();
    }

    // Вызывается только 1 раз - после нажатия stop
    @Override
    public void stop() {
        // Остановка всех моторов
        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        blMotor.setPower(0.0);
        brMotor.setPower(0.0);

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}