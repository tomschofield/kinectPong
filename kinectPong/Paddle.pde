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
  void update(float newY) {
    y=newY;
    vel.x=x-px;
    vel.y=y-py;
   
    px=x;
    py=y;
  }
  void drawPaddle(){
   rect (x,y,w,h);
  }
  void addPoint() {
    points++;
  }
  
}

