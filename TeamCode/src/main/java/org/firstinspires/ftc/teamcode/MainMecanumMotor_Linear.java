// Основной класс для управления роботом в TeleOp режиме
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// import com.qualcomm.robotcore.hardware.DcMotorSimple;
// import com.qualcomm.robotcore.hardware.Servo;
// import com.qualcomm.robotcore.util.Range;


@TeleOp(name="DriveMecanumTeleOp")
public class MainMecanumMotor_Linear extends LinearOpMode {
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

    // Вбок
    // По умолчанию: Левый стик X, инверсия по умолчанию
    private final int lateralAxisID = 1;
    private final double lateralDirection = -1.0;

    // Поворот
    // По умолчанию: Правый стик X, инверсия по умолчанию
    private final int yawAxisID = 3;
    private final double yawDirection = -1.0;

//    static final double SERVO_START_POS = 0.9;
//    static final double SERVO_INCREMENT = 0.001;
//    static final double SERVO_MAX       = 1.0;
//    static final double SERVO_MIN       = 0.0;
//
//    Servo servo;
//    double position = SERVO_START_POS;
//
//    private DcMotor motor;

    // Метод, который сопоставляет ID из конфига с фактической осью геймпада
    private double getGamepadAxis(int axisID) {
        switch (axisID) {
            case 0: return gamepad1.left_stick_y;
            case 1: return gamepad1.left_stick_x;
            case 2: return gamepad1.right_stick_y;
            case 3: return gamepad1.right_stick_x;
            // Триггеры (вдруг понадобятся)
            // case 4: return gamepad1.left_trigger;
            // case 5: return gamepad1.right_trigger;
            default:
                return 0.0;
        }
    }

    @Override
    public void runOpMode() {
        flMotor = hardwareMap.get(DcMotor.class, "front_left_motor");
        frMotor = hardwareMap.get(DcMotor.class, "front_right_motor");
        blMotor = hardwareMap.get(DcMotor.class, "back_left_motor");
        brMotor = hardwareMap.get(DcMotor.class, "back_right_motor");

        flMotor.setDirection(DcMotor.Direction.REVERSE);
        blMotor.setDirection(DcMotor.Direction.REVERSE);
        frMotor.setDirection(DcMotor.Direction.FORWARD);
        brMotor.setDirection(DcMotor.Direction.FORWARD);

//        servo = hardwareMap.get(Servo.class, "servo");
//        servo.setPosition(SERVO_START_POS);
//
//        motor = hardwareMap.get(DcMotor.class, "motor");
//        motor.setDirection(DcMotor.Direction.FORWARD);

        // С помощью telemetry мы можем устанавливать статус и не только, вот пример
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Моторы на ноль
        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        blMotor.setPower(0.0);
        brMotor.setPower(0.0);

        // Меняем статус
        telemetry.addData("Status", "Тест моторов");
        telemetry.update();

        // мотор будет вращаться пока мы не нажмем STOP
        while (opModeIsActive()) {
            // Управление с геймпада
            double axial = getGamepadAxis(axialAxisID) * axialDirection;
            double lateral = getGamepadAxis(lateralAxisID) * lateralDirection;
            double yaw = getGamepadAxis(yawAxisID) * yawDirection;

            // Математика Mercan
            double flPower = axial + lateral + yaw;
            double frPower = axial - lateral - yaw;
            double blPower = axial - lateral + yaw;
            double brPower = axial + lateral - yaw;

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

            flMotor.setPower(flPower);
            frMotor.setPower(frPower);
            blMotor.setPower(blPower);
            brMotor.setPower(brPower);

//            if (gamepad1.cross) {
//                position = 1.0;
//                motor.setPower(0.1);
//
//            }
//            if (!gamepad1.cross) {
//                position = 0.9;
//                motor.setPower(0.0);
//            }
//
//            position = Range.clip(position, SERVO_MIN, SERVO_MAX);
//            servo.setPosition(position);

            telemetry.addData("Status", "Тест моторов и сервы");
            telemetry.addData("flPower", "%.2f", flPower);
            telemetry.addData("frPower", "%.2f", frPower);
            telemetry.addData("blPower", "%.2f", blPower);
            telemetry.addData("brPower", "%.2f", brPower);
//            telemetry.addData("ServoPS", "%.2f", position);
            telemetry.update();
        }

        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        blMotor.setPower(0.0);
        brMotor.setPower(0.0);
    }
}
