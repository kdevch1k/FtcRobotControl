package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Test_Omni2_Lineral2")
public class TestOmni_x2_and_Lineral_x2 extends LinearOpMode {

    private DcMotor LeftMotor;
    private DcMotor RightMotor;

    @Override
    public void runOpMode() {
        LeftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        RightMotor = hardwareMap.get(DcMotor.class, "right_motor");

        LeftMotor.setDirection(DcMotor.Direction.REVERSE);
        RightMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Моторы на ноль
        LeftMotor.setPower(0.0);
        RightMotor.setPower(0.0);

        // Меняем статус
        telemetry.addData("Status", "Тест моторов");
        telemetry.update();


        while (opModeIsActive()) {
            double leftY = -gamepad1.left_stick_y;
            double leftX = gamepad1.left_stick_x;

            double LeftPower = leftY + leftX;
            double RightPower = leftY - leftX;

            double max;
            max = Math.max(Math.abs(LeftPower), Math.abs(RightPower));

            if (max > 1.0) {
                LeftPower /= max;
                RightPower /= max;
            }

            LeftMotor.setPower(LeftPower);
            RightMotor.setPower(RightPower);

            telemetry.addData("Status", "Тест моторов");
            telemetry.addData("LeftPower", LeftPower);
            telemetry.addData("RightPower", RightPower);
            telemetry.update();
        }
    }
}
