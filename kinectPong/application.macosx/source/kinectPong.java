import processing.core.*; 
import processing.xml.*; 

import controlP5.*; 
import SimpleOpenNI.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class kinectPong extends PApplet {




SimpleOpenNI  context;
TSSkeleton skeleton;
ControlP5 cp5;

Paddle[] paddles;
Ball ball;
float timer=0;
DropdownList d1, d2, d3, d4;

int paddleSelector1=15;
int paddleSelector2=9;
String jointNames[];
int translations[];
boolean onePlayer=true;
PFont font;

public void setup() {
  size(800, 500);
  ball= new Ball(width/2, height/2, new PVector(5, 5));
  rectMode(CENTER);
  paddles=new Paddle[2];
  paddles[0]= new Paddle(width-40, 20);
  paddles[1]= new Paddle(40, 20);

  setupContext();
  skeleton= new TSSkeleton();
  cp5 = new ControlP5(this);
//  cp5.addBang("restart").setPosition(350, 10).setSize(10, 10);

  d1 = cp5.addDropdownList("paddle1").setPosition(width-150, 20);
  d2 = cp5.addDropdownList("paddle2").setPosition(50, 20);
  d3 = cp5.addDropdownList("1_or_2_player").setPosition(150, 20);
  d4 = cp5.addDropdownList("speed").setPosition(250, 20);

  jointNames = loadStrings("joints.txt");

  d4.addItem("slow", 0);
  d4.addItem("fast", 1);
  d4.addItem("Jeez-us", 2);

  for (int i=0;i<jointNames.length;i++) {
    d1.addItem(jointNames[i], i);
    d2.addItem(jointNames[i], i);
  }

  d3.addItem("one player", 0);
  d3.addItem("two players", 1);

  smooth();
  println( "SimpleOpenNI.SKEL_HEAD "+SimpleOpenNI.SKEL_HEAD );
  println( "SimpleOpenNI.SKEL_LEFT_ANKLE "+SimpleOpenNI.SKEL_LEFT_ANKLE );
  println( "SimpleOpenNI.SKEL_LEFT_COLLAR "+SimpleOpenNI.SKEL_LEFT_COLLAR );
  println( "SimpleOpenNI.SKEL_LEFT_ELBOW "+SimpleOpenNI.SKEL_LEFT_ELBOW );
  println( "SimpleOpenNI.SKEL_LEFT_FINGERTIP "+SimpleOpenNI.SKEL_LEFT_FINGERTIP );
  println( "SimpleOpenNI.SKEL_LEFT_FOOT "+SimpleOpenNI.SKEL_LEFT_FOOT );
  println( "SimpleOpenNI.SKEL_LEFT_HAND "+SimpleOpenNI.SKEL_LEFT_HAND );
  println( "SimpleOpenNI.SKEL_LEFT_HIP "+SimpleOpenNI.SKEL_LEFT_HIP );
  println( "SimpleOpenNI.SKEL_LEFT_KNEE "+SimpleOpenNI.SKEL_LEFT_KNEE);
  println( "SimpleOpenNI.SKEL_LEFT_SHOULDER "+SimpleOpenNI.SKEL_LEFT_SHOULDER );
  println( "SimpleOpenNI.SKEL_LEFT_WRIST "+SimpleOpenNI.SKEL_LEFT_WRIST );
  println( "SimpleOpenNI.SKEL_NECK "+SimpleOpenNI.SKEL_NECK );
  println( "SimpleOpenNI.SKEL_RIGHT_ANKLE "+SimpleOpenNI.SKEL_RIGHT_ANKLE );
  println( "SimpleOpenNI.SKEL_RIGHT_COLLAR "+SimpleOpenNI.SKEL_RIGHT_COLLAR );
  println( "SimpleOpenNI.SKEL_RIGHT_ELBOW "+SimpleOpenNI.SKEL_RIGHT_ELBOW );
  println( "SimpleOpenNI.SKEL_RIGHT_FINGERTIP "+SimpleOpenNI.SKEL_RIGHT_FINGERTIP );
  println( "SimpleOpenNI.SKEL_RIGHT_FOOT "+SimpleOpenNI.SKEL_RIGHT_FOOT );
  println( "SimpleOpenNI.SKEL_RIGHT_HAND  "+SimpleOpenNI.SKEL_RIGHT_HAND );
  println( "SimpleOpenNI.SKEL_RIGHT_HIP "+SimpleOpenNI.SKEL_RIGHT_HIP );
  println( "SimpleOpenNI.SKEL_RIGHT_KNEE "+SimpleOpenNI.SKEL_RIGHT_KNEE );
  println( "SimpleOpenNI.SKEL_RIGHT_SHOULDER "+SimpleOpenNI.SKEL_RIGHT_SHOULDER );
  println( "SimpleOpenNI.SKEL_RIGHT_WRIST "+SimpleOpenNI.SKEL_RIGHT_WRIST );
  println( "SimpleOpenNI.SKEL_TORSO "+SimpleOpenNI.SKEL_TORSO );
  println( "SimpleOpenNI.SKEL_WAIST "+SimpleOpenNI.SKEL_WAIST );

  translations=new int[jointNames.length];
  translations[0]=1 ;
  translations[1]= 19;
  translations[2]=5 ;
  translations[3]=7 ;
  translations[4]=10 ;
  translations[5]= 20;
  translations[6]=9 ;
  translations[7]=17 ;
  translations[8]=18 ;
  translations[9]=6 ;
  translations[10]=8 ;
  translations[11]=2 ;
  translations[12]=23 ;
  translations[13]= 11;
  translations[14]=13 ;
  translations[15]= 16;
  translations[16]= 24;
  translations[17]= 15;
  translations[18]= 21;
  translations[19]= 22;
  translations[20]= 12;
  translations[21]= 14;
  translations[22]= 3;
  translations[23]= 4;

  font=loadFont("AlBayan-48.vlw");
  textFont(font, 48);
}


public void draw() {
  background(0);
  fill(0, 255, 0);
  stroke(255);
  // get kinect color image
  context.update();
  // scale to an arbitrary size and position (e.g. scale down 75%, and align to bottom / center)
  skeleton.updateSkeleton();


  if (onePlayer) {
    paddles[0].update(skeleton.getScreenCoords(1, paddleSelector1).y);
    paddles[1].update(skeleton.getScreenCoords(1, paddleSelector2).y);
  }
  else {
    paddles[0].update(skeleton.getScreenCoords(1, paddleSelector1).y);
    try {
      paddles[1].update(skeleton.getScreenCoords(2, paddleSelector2).y);
    }
    catch(Exception e) {
    }
  }

  for (int i=0;i<paddles.length;i++) {

    paddles[i].drawPaddle();
  }
  ball.update();
  ball.checkCollision(paddles);
  ball.drawBall();

  /*PVector rHand = skeleton.getScreenCoords(1, SimpleOpenNI.SKEL_RIGHT_HAND) ;
   PVector lHand = skeleton.getScreenCoords(1, SimpleOpenNI.SKEL_LEFT_HAND) ;
   fill(255, 0, 0);
   ellipse(rHand.x, rHand.y, 20, 20);
   ellipse(lHand.x, lHand.y, 20, 20);*/
  float offset=80;
  text(str(paddles[0].points), width-(offset+(0.5f*textWidth(str(paddles[0].points)))), height -50);
  text(str(paddles[1].points), offset, height -50);
  fill(0, 255, 0, 255 -( (255/(timer+1)) *(timer+1)));

  if (timer>0) {
    String txt="POINT";
    text(txt, (width/2)-(0.5f*textWidth(txt)), height/2);
    timer--;
  }
}

public void setupContext() {
  //setup onenNI context
  context = new SimpleOpenNI(this);
  // enable depthMap generation 
  context.enableDepth();
  // enable camera image generation
  context.enableRGB();
  context.setMirror(true);

  // enable skeleton generation for all joints
  context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
  stroke(255, 255, 255);
  smooth();
}


//OPENNI CALLBACKS
public void onNewUser(int userId)
{
  println("onNewUser - userId: " + userId);
  println("  start pose detection");


  context.requestCalibrationSkeleton(userId, true);
}

public void onLostUser(int userId)
{
  println("onLostUser - userId: " + userId);
}

public void onExitUser(int userId)
{
  println("onExitUser - userId: " + userId);
}

public void onReEnterUser(int userId)
{
  println("onReEnterUser - userId: " + userId);
}


public void onStartCalibration(int userId)
{
  println("onStartCalibration - userId: " + userId);
}

public void onEndCalibration(int userId, boolean successfull)
{
  println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

  if (successfull) 
  { 
    println("  User calibrated !!!");
    context.startTrackingSkeleton(userId);
  } 
  else 
  { 
    println("  Failed to calibrate user !!!");
    println("  Start pose detection");
    context.startPoseDetection("Psi", userId);
  }
}

public void onStartPose(String pose, int userId)
{
  println("onStartdPose - userId: " + userId + ", pose: " + pose);
  println(" stop pose detection");

  context.stopPoseDetection(userId); 
  context.requestCalibrationSkeleton(userId, true);
}

public void onEndPose(String pose, int userId)
{
  println("onEndPose - userId: " + userId + ", pose: " + pose);
}

public void controlEvent(ControlEvent theEvent) {
  // DropdownList is of type ControlGroup.
  // A controlEvent will be triggered from inside the ControlGroup class.
  // therefore you need to check the originator of the Event with
  // if (theEvent.isGroup())
  // to avoid an error message thrown by controlP5.

/*  if (theEvent.getController().getName().equals("restart")) {
    paddles[0].points=0;
    paddles[1].points=0;
  }*/

  if (theEvent.isGroup()) {
    // check if the Event was triggered from a ControlGroup
    //  println("event from group : "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
    if (theEvent.getGroup().toString().equals("paddle1 [DropdownList]")) {
      println("paddle1 "+theEvent.getGroup().getValue());
      paddleSelector1 = translations[(int) theEvent.getGroup().getValue()];
      println(jointNames[paddleSelector1]);
    }
    else if (theEvent.getGroup().toString().equals("paddle2 [DropdownList]")) {
      println("paddle2 "+theEvent.getGroup().getValue());
      paddleSelector2 = translations[(int)theEvent.getGroup().getValue()];
      println(jointNames[paddleSelector2]);
    }
    else if (theEvent.getGroup().toString().equals("1_or_2_player [DropdownList]")) {
      if ((int)theEvent.getGroup().getValue()==0) {
        onePlayer=true;
      }
      else {
        onePlayer=false;
      }
    }
    else if (theEvent.getGroup().toString().equals("speed [DropdownList]")) {
      if ((int)theEvent.getGroup().getValue()==0) {
        ball.speed=new PVector(5, 5);
      }
      else if ((int)theEvent.getGroup().getValue()==1) {
        ball.speed=new PVector(10, 10);
      }
      else if ((int)theEvent.getGroup().getValue()==2) {
        ball.speed=new PVector(15, 15);
      }
    }
  } 
  else if (theEvent.isController()) {
    println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController());
  }
}

class Ball
{
  float x, y;
  PVector speed;
  int w=10;
  int h=10;
  float initialYspeed;
  Ball(float xin, float yin, PVector speedin)
  {
    x = xin;
    y = yin;
    speed = speedin;
    initialYspeed=speedin.y;
  }
  public void update() {
    x+=speed.x;
    y+=speed.y;
    // if(y>initialYspeed){
    //  y-=0.1; 
    // }
  }

  public void restart() {
    x=width/2;
    y=height/2;
  }

  public void drawBall() {

    ellipse(x, y, w, h);
  }
  public void checkCollision(Paddle[] pads) {

    //check against walls
    if (y>=height||y<0) {
      speed.y*=-1;
    }
    if (x>=width) {
      restart();
      paddles[1].addPoint();
      timer=40;
    }
    if ( x<0) {
      restart();
      paddles[0].addPoint();
      timer=40;
    }
    //check against paddles
    for (int i=0;i<pads.length;i++) {
      if (x> pads[i].x-(pads[i].w/2) && x<pads[i].x+(pads[i].w/2) && y> pads[i].y-(pads[i].h/2) && y<pads[i].y+(pads[i].h/2)  ) {
        speed.x*=-1;

        float friction=0.5f;
        println(pads[i].vel.y);
        speed.y+=friction*pads[i].vel.y;
      }
    }
  }
}

class Paddle
{
  float x, y;
  float px,py;
  int w=10;
  int h=120;
  PVector vel;
  int points=0;
  Paddle(float xin, float yin)
  {
    x = xin;
    y = yin;
    vel=new PVector(0,0);
  }
  public void update(float newY) {
    y=newY;
    vel.x=x-px;
    vel.y=y-py;
   
    px=x;
    py=y;
  }
  public void drawPaddle(){
   rect (x,y,w,h);
  }
  public void addPoint() {
    points++;
  }
  
}


class TSJoint {
  //----------------------------------
  PVector [] buffer;
  PVector smoothedVelocity;
  //velocity this frame - unsmoothed
  PVector velocity;
  PVector currentPos;
  PVector prevPos;
  //for the circular buffer
  int bufferIndex;

  TSJoint(int smoothingAmt) {
    
    buffer= new PVector[smoothingAmt];

    for (int i=0;i<buffer.length;i++) {
      buffer[i]=new PVector(0, 0, 0);
    }
    velocity=new PVector(0, 0, 0);
    currentPos=new PVector(0, 0, 0);
    prevPos=new PVector(0, 0, 0);
    smoothedVelocity=new PVector(0, 0, 0);

    bufferIndex=0;
  }
  public void updateAll(PVector newPos) {
    //update current joint position
    currentPos=newPos;
    //map velocity to depth image size - I can't find how to get the depth of this context
    velocity.x= map( newPos.x-prevPos.x, -context.depthWidth(), context.depthWidth(), -1, 1);
    velocity.y= map(newPos.y-prevPos.y, -context.depthHeight(), context.depthHeight(), -1, 1);
    velocity.z= newPos.z-prevPos.z;
    
    //overwrite the circular buffer
    buffer[bufferIndex]=velocity;
  //and incrememnt the buffer index
    bufferIndex++;
    if (bufferIndex>=buffer.length) {
      bufferIndex=0;
    }
    prevPos=newPos;
    smoothedVelocity=getSmoothedVelocity();
  }

  public PVector getSmoothedVelocity() {

    PVector total=new PVector(0, 0, 0);
    for (int i=0;i<buffer.length;i++) {
      total.x+=buffer[i].x;
      total.y+=buffer[i].y;
      total.z+=buffer[i].z;
    }

    PVector smthd= new PVector(total.x/buffer.length, total.y/buffer.length, total.z/buffer.length  );
    return smthd;
  }
};


class TSSkeleton {

  // a skeleton
  // store positions and velocities, scaled and mapped to screen space (in case we have scaled down the kinect input)
  //openni kinect capture / scale / skeleton 
  int userCount;
  PFont debugFont;
  //first dimension is users, second dimension is all velocity and positions for each joint
  TSJoint[][] tsjoints;
  //24 joints are tracked according to documentation, can't find a constant that represents this.
  int numJoints = 24;
  int max_users=17;

  PVector test=new PVector(0, 0, 0);
  //----------------------------------

  TSSkeleton() {
    
    userCount=0;
    debugFont=loadFont("AlBayan-48.vlw");

    tsjoints= new TSJoint[max_users][numJoints];
    //set all initial previous joint positions and velocities to 0
    for (int i=0;i<max_users;i++) {
      for (int j=0;j<tsjoints.length;j++) {
        tsjoints[i][j]=new TSJoint(20);
      }
    }
  }

  public void updateSkeleton() {
    userCount = context.getNumberOfUsers();
    updateVelocities();
  }
  //for debugging purposes
  public void drawAllSkeletons() {
    pushMatrix();
    translate(width/2, height/2, 0);
    //openni draws upside down
    rotateX(PI);
    scale(0.5f);
    translate(0, 0, -1000); 
    stroke(100); 

    int[] userList = context.getUsers();
    for (int i=0;i<userList.length;i++)
    {
      if (context.isTrackingSkeleton(userList[i]))
        drawSkeleton(userList[i]);
    }
    popMatrix();
  }

  //from simpleOPENNI examples/ credits to http://code.google.com/p/simple-openni
  public void drawSkeleton(int userId) {
    strokeWeight(3);

    // to get the 3d joint data
    drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

    drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
    drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
    drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

    drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
    drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
    drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

    drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
    drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

    drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
    drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
    drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

    drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
    drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
    drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);
  }
  //from simpleOPENNI examples/ credits to http://code.google.com/p/simple-openni
  public void drawLimb(int userId, int jointType1, int jointType2)
  {
    PVector jointPos1 = new PVector();
    PVector jointPos2 = new PVector();
    float  confidence;

    // draw the joint position
    confidence = context.getJointPositionSkeleton(userId, jointType1, jointPos1);
    confidence = context.getJointPositionSkeleton(userId, jointType2, jointPos2);

    stroke(255, 0, 0, confidence * 200 + 55);
    line(jointPos1.x, jointPos1.y, jointPos1.z, 
    jointPos2.x, jointPos2.y, jointPos2.z);
  }
  //////////////GIVEN A JOINT ID THIS WILL GIVE THE POSITION RELATIVE TO DEPTH MAP SIZE (640*480)///////////////////////////////
  public PVector getScreenCoords(int userId, int jointType) {
    //JOINT POS IN WORLD SIZE IE MM
    PVector jointPos = new PVector();
    //JOINT POS IN SCEEN SIZE IE PIXELS
    PVector jointPos_Proj = new PVector(); 
    context.getJointPositionSkeleton(userId, jointType, jointPos);
    if (context.isTrackingSkeleton(userId)) {
      context.convertRealWorldToProjective(jointPos, jointPos_Proj);
    }
    return(jointPos_Proj);
  }
  //////////////GIVEN A JOINT ID A POSITION AND A NEW WIDTH AND HEIGH THIS WILL GIVE THE POSITION RELATIVE TO THAT NEW SIZE///////////////////////////////
  public PVector getMappedCoords(int userId, int jointType, int x, int y, int w, int h) {
    //JOINT POS IN WORLD SIZE IE MM
    PVector jointPos = new PVector();
    //JOINT POS IN SCEEN SIZE IE PIXELS
    PVector jointPos_Proj = new PVector(); 
    context.getJointPositionSkeleton(userId, jointType, jointPos);
    if (context.isTrackingSkeleton(userId)) {
      context.convertRealWorldToProjective(jointPos, jointPos_Proj);
    }
    float xm, ym, zm;
    xm=x+map(jointPos_Proj.x, 0, context.depthWidth(), 0, w);
    ym=y+map(jointPos_Proj.y, 0, context.depthHeight(), 0, h);
    zm=jointPos_Proj.z;

    return(new PVector(xm, ym, zm));
  }
  public PVector getWorldCoords(int userId, int jointType) {
    //JOINT POS IN WORLD SIZE IE MM
    PVector jointPos = new PVector();
    context.getJointPositionSkeleton(userId, jointType, jointPos);

    return(jointPos);
  }
  //am example to test velocity data
  public void drawDebugInfo() {
    fill(255);
    textFont(debugFont, 48);
    int[] userList = context.getUsers();
    if (userList.length>0) {
      for (int i=0;i<userList.length;i++) {

        // for (int i=0;i<20;i++) {
        PVector jointPos1 = new PVector();

        context.getJointPositionSkeleton(userList[i], SimpleOpenNI.SKEL_RIGHT_HAND, jointPos1);

        if (context.isTrackingSkeleton(userList[i])) {
          PVector jointPos_Proj = new PVector(); 
          context.convertRealWorldToProjective(jointPos1, jointPos_Proj);
          try {
            text("right hand_VEL_X "+tsjoints[userList[i]][SimpleOpenNI.SKEL_RIGHT_HAND].smoothedVelocity.x, 100, height- 150);
            if (tsjoints[userList[i]][SimpleOpenNI.SKEL_RIGHT_HAND].smoothedVelocity.x>0) {
              text("right hand going LEFT ", 100, height- 50);
            }
            else {
              text("right hand going RIGHT ", 100, height- 50);
            }
            if (  tsjoints[userList[i]][SimpleOpenNI.SKEL_RIGHT_HAND].smoothedVelocity.y<0) {
              text("right hand going UP ", 100, height- 100);
            }
            else {
              text("right hand going DOWN ", 100, height-100);
            }
          }
          catch(Exception e) {
            println(e);
          }
        }
      }
    }
  }
  public void updateVelocities() {
    int[] userList = context.getUsers();

    //for each user
    for (int i=0;i<userList.length;i++) {
      //if there is a valid skeleton
      if (context.isTrackingSkeleton(userList[i])) {
        //for each joint on that skeleton
        for (int j=0;j<tsjoints.length;j++) {
          PVector jointPos = new PVector();
          //get real world coords
          context.getJointPositionSkeleton(userList[i], j, jointPos);
          PVector jointPos_Proj = new PVector(); 
          //convert to screen coords
          context.convertRealWorldToProjective(jointPos, jointPos_Proj);

          tsjoints[userList[i]][j].updateAll(jointPos_Proj);
        }
      }
    }
  }
};

/*static int	SKEL_HEAD 
 static int	SKEL_LEFT_ANKLE 
 static int	SKEL_LEFT_COLLAR 
 static int	SKEL_LEFT_ELBOW 
 static int	SKEL_LEFT_FINGERTIP 
 static int	SKEL_LEFT_FOOT 
 static int	SKEL_LEFT_HAND 
 static int	SKEL_LEFT_HIP 
 static int	SKEL_LEFT_KNEE 
 static int	SKEL_LEFT_SHOULDER 
 static int	SKEL_LEFT_WRIST 
 static int	SKEL_NECK 
 static int	SKEL_RIGHT_ANKLE 
 static int	SKEL_RIGHT_COLLAR 
 static int	SKEL_RIGHT_ELBOW 
 static int	SKEL_RIGHT_FINGERTIP 
 static int	SKEL_RIGHT_FOOT 
 static int	SKEL_RIGHT_HAND 
 static int	SKEL_RIGHT_HIP 
 static int	SKEL_RIGHT_KNEE 
 static int	SKEL_RIGHT_SHOULDER 
 static int	SKEL_RIGHT_WRIST 
 static int	SKEL_TORSO 
 static int	SKEL_WAIST */
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "kinectPong" });
  }
}
