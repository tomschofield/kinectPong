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
  void update() {
    x+=speed.x;
    y+=speed.y;
    // if(y>initialYspeed){
    //  y-=0.1; 
    // }
  }

  void restart() {
    x=width/2;
    y=height/2;
  }

  void drawBall() {

    ellipse(x, y, w, h);
  }
  void checkCollision(Paddle[] pads) {

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

        float friction=0.5;
        println(pads[i].vel.y);
        speed.y+=friction*pads[i].vel.y;
      }
    }
  }
}

