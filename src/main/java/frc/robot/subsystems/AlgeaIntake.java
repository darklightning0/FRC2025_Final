// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import java.rmi.Remote;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgeaIntake extends SubsystemBase {

  public static TalonSRX rotationMotor = new TalonSRX(Constants.AlgInt_Rotation);
  public static VictorSPX leftNEO = new VictorSPX(Constants.AlgInt_LeftNEO);
  public static VictorSPX rightNEO = new VictorSPX(Constants.AlgInt_RightNEO);

  private static double rotationSpeed = 0;
  private static double neoSpeed = 0;


  public AlgeaIntake(){

    rotationMotor.setInverted(false);
    leftNEO.setInverted(false);
    rightNEO.setInverted(false);

    rotationMotor.setNeutralMode(NeutralMode.Brake);
    leftNEO.setNeutralMode(NeutralMode.Coast);
    rightNEO.setNeutralMode(NeutralMode.Coast);

  }


  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
   
    rotationSpeed = 0.2;

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}