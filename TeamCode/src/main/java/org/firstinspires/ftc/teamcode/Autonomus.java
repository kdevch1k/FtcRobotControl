package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Robot: Small", group="Robot")
public class Autonomus extends LinearOpMode {
    private DcMotor flMotor;
    private DcMotor frMotor;
    private DcMotor blMotor;
    private DcMotor brMotor;

    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 1;

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


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.

        // Step 1:  Drive forward for 3 seconds
        flMotor.setPower(FORWARD_SPEED);
        blMotor.setPower(FORWARD_SPEED);
        frMotor.setPower(FORWARD_SPEED);
        brMotor.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }
}
