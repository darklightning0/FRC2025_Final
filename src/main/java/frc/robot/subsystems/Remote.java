package frc.robot.subsystems;

import static frc.robot.Constants.ControllerConstants.driveJoystick;
import static frc.robot.Constants.ControllerConstants.driverJoystickDef;
import static frc.robot.Constants.ControllerConstants.operatorJoystick;
import static frc.robot.Constants.ControllerConstants.operatorJoystickDef;


import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.ControllerConstants;
//import frc.robot.Constants.SubsystemConstants.RemoteButtons;
//import frc.robot.Constants.SubsystemConstants.RemoteOperatorButtons;

enum DriveMode {

}

public final class Remote {
    public static enum ClimbMode {
        Idle,
        Open,
        Close,
    }
    public static enum rotationMode {
        ground,
        parallel,
        idle,
    }
    public static enum DriveMode {
        MedGear,
        HighGear
    }
    public static enum BoosterDirection {
        Negative,
        Positive,
    }


    private static Remote single_instance = null;
    
    final ShuffleboardTab remoteInput;
    // final GenericEntry shuffle_driveForward, shuffle_intakeSpeed, shuffle_shooterSpeed; //, shuffle_driveRotation;
    final GenericEntry shuffle_driveKP, shuffle_driveKF; //, shuffle_driveRotation;

    static double input_driveForward = 0.,
           input_driveRotation = 0.,
           input_shooter = 0.;
    static ClimbMode input_climb = ClimbMode.Idle;
    static rotationMode input_intake = rotationMode.Idle;
    static BoosterDirection input_boosterDir = BoosterDirection.Positive;
    static DriveMode input_driveMode = DriveMode.MedGear;
    static boolean input_ampShoot = false;
    static boolean input_driveSlowTurn = false;
    static boolean input_shooterAuto = false;


    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private Remote() {
        remoteInput = Shuffleboard.getTab("Remote Input");

            shuffle_driveKP = remoteInput.add("Drive KP", DriveTrain.simplePid.getP()
            )
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", -1, "max", 1))
                .getEntry();
            shuffle_driveKF = remoteInput.add("Drive KF", DriveTrain.simplePid.getF())
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", -1, "max", 1))
                .getEntry();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized Remote getInstance()
    {
        if (single_instance == null) single_instance = new Remote();
        return single_instance;
    }

    /* public static enum POV {
        N, NE, E, SE, S, SW, W, NW, NONE
    }

    public static enum BoosterMode {
        INTAKE, SHOOTER, CLIMB
    } */

    /* private static final int
        POV_North = 0, POV_NorthEast = 45,
        POV_East = 90, POV_SouthEast = 135,
        POV_South = 180, POV_SouthWest = 225,
        POV_West = 270, POV_NorthWest = 315; */

    public double getShooterSpeed() {
        return input_shooter;
        // return shuffle_shooterSpeed.getDouble(0);
        // return (boosterMode == BoosterMode.SHOOTER) ? getBoosterValue() : 0;
    } 

    public static ClimbMode getClimbMode() {
        return input_climb;
    }

    public rotationMode getrotationMode() {
        return input_intake;
    }

    public static double getDriveForward() {
        return input_driveForward;
    }

    public static double getDriveRotation() {
        return input_driveRotation;
    }

    // pos. direction: 0 ~ 1
    // neg. direction: 0 ~ -1
    private static double getBoosterValue() {
        if (!driverJoystickDef.isConnected()) return 0;
        double value = (1 - Constants.ControllerConstants.driverJoystick.getRawAxis(3))/2;
        if (value != 0) return getBoosterDirectionInternal() ? value : -value;
        else return getBoosterDirectionInternal() ? 0.01 : -0.01;
    }

    public static double getDriverBoosterValue() {
        return driverJoystickDef.isConnected() ? getBoosterValue() : 0.;
    }

    public static boolean getDriveSlowTurn() {
        return input_driveSlowTurn;
    }


    // true: default (positive)
    // false: reversed (negative)
    private static boolean getBoosterDirectionInternal() {
        return !Constants.ControllerConstants.driverJoystickDef.getRawButton(Constants.SubsystemConstants.RemoteButtons.ShooterRevert);
    }

    public static BoosterDirection getBoosterDirection() {
        return input_boosterDir;
    }
    public static double getBoosterDirectionDouble() {
        return getBoosterDirection() == BoosterDirection.Positive ? 1. : -1.;
    }

    public static boolean getAmpShoot() {
        return input_ampShoot;
    }
    public static boolean getShooterAuto() {
        return input_shooterAuto;
    }

    // true: high gear
    // false: low gear
    // public static boolean getDriveHighGear() {
    //     return ControllerConstants.operatorJoystickDef.getRawButton(8);
    // }

    /* public static POV getPOV() {
        switch (operatorJoystickDef.getPOV()) {
            case POV_North:
                return POV.N;
            case POV_NorthEast:
                return POV.NE;
            case POV_East:
                return POV.E;
            case POV_SouthEast:
                return POV.SE;
            case POV_South:
                return POV.S;
            case POV_SouthWest:
                return POV.SW;
            case POV_West:
                return POV.W;
            case POV_NorthWest:
                return POV.NW;
            default:
                return POV.NONE;
        }
    } */

    private static boolean takeoverEnabled = false; 

    public static void mainloop() {
        if (driverJoystickDef.getRawButtonPressed(RemoteButtons.TakeoverToggle)) {
            takeoverEnabled = !takeoverEnabled;
        }
        takeoverEnabled = true;

        if (takeoverEnabled && driverJoystickDef.isConnected()) {
            input_driveSlowTurn = driverJoystickDef.getRawButton(RemoteButtons.DriveSlowTurn);
            input_driveForward = -driverJoystick.getY();
            // input_driveRotation = driverJoystick.getZ();
            input_driveRotation = driverJoystick.getX() * 0.25 + driverJoystick.getZ() * 0.8;
            input_driveMode = driverJoystickDef.getRawButton(9) ? DriveMode.HighGear : DriveMode.MedGear;
        } else if (operatorJoystickDef.isConnected()) {
            input_driveSlowTurn = false;
            input_driveForward = operatorJoystick.getY();
            input_driveRotation = operatorJoystick.getZ();
            input_driveMode = operatorJoystickDef.getRawButton(8) ? DriveMode.HighGear : DriveMode.MedGear;
        } else {
            input_driveSlowTurn = false;
            input_driveForward = 0.;
            input_driveRotation = 0.;
            input_driveMode = DriveMode.MedGear;
        }

        if (operatorJoystickDef.isConnected()) {
            rotationMode intake_mode = rotationMode.Idle;
            if (operatorJoystickDef.getRawButton(RemoteOperatorButtons.Emphasize)) {
                if (operatorJoystickDef.getRawButton(RemoteOperatorButtons.Silence)) {
                    intake_mode = rotationMode.Med;
                } else {
                    intake_mode = rotationMode.Fast;
                }
            } else if 
            (operatorJoystickDef.getRawButton(RemoteOperatorButtons.Silence)) {
                intake_mode = rotationMode.Slow;
            } else {
                intake_mode = rotationMode.Med;
            }

            if (operatorJoystickDef.getRawButton(RemoteOperatorButtons.Intake)) {
                input_intake = intake_mode;
            } else if (operatorJoystickDef.getRawButton(RemoteOperatorButtons.IntakeShooter)) {
                input_intake = intake_mode;
                // to be implemented
                // input_shooter = ...;
            } else {
                input_intake = rotationMode.Idle;
            }

            if (operatorJoystickDef.getRawButton(RemoteOperatorButtons.AmpShoot)) {
                input_ampShoot = true;
            } else {
                input_ampShoot = false;
            }

            input_shooter = (operatorJoystick.getX() + operatorJoystick.getZ()) / 2.;
            input_shooterAuto = operatorJoystickDef.getRawButton(9);
            
            input_boosterDir = operatorJoystickDef.getRawButton(RemoteOperatorButtons.ShooterRevert) ? BoosterDirection.Negative : BoosterDirection.Positive;
            if (input_boosterDir == BoosterDirection.Negative) input_shooter *= -1.;
        } else if (driverJoystickDef.isConnected()) {
            if (driverJoystickDef.getRawButton(RemoteButtons.BoosterIntake)) {
                if (driverJoystickDef.getRawButton(RemoteButtons.IntakeFast)) {
                    input_intake = rotationMode.ground;
                } else {
                    input_intake = rotationMode.Med;
                }
            } else {
                if (driverJoystickDef.getRawButton(RemoteButtons.IntakeFast)) {
                    input_intake = rotationMode.Slow;
                } else {
                    input_intake = rotationMode.Idle;
                }
            }

            if (driverJoystickDef.getRawButtonPressed(RemoteButtons.AmpShoot)) {
                input_ampShoot = !input_ampShoot;
            }

            input_shooter = getBoosterValue();
            input_shooterAuto = driverJoystickDef.getRawButton(2);
            input_boosterDir = getBoosterDirectionInternal() ? BoosterDirection.Positive : BoosterDirection.Negative;
        } else {
            input_intake = rotationMode.Idle;
            input_ampShoot = false;
            input_shooter = 0;
            input_shooterAuto = false;
        }

        if (driverJoystickDef.isConnected()) {
            // todo: remove this
            // input_shooter = getBoosterValue();

            if (driverJoystickDef.getRawButton(RemoteButtons.ClimbOpen)) {
                input_climb = ClimbMode.Open;
            } else if (driverJoystickDef.getRawButton(RemoteButtons.ClimbClose)) {
                input_climb = ClimbMode.Close;
            } else {
                input_climb = ClimbMode.Idle;
            }

            // input_boosterDir = getBoosterDirectionInternal() ? BoosterDirection.Positive : BoosterDirection.Negative;
        } else {
            // input_shooter = 0.;
            input_climb = ClimbMode.Idle;
            // input_boosterDir = BoosterDirection.Positive;
        }


        // shuffle_driveKF.getDouble();
    }
}