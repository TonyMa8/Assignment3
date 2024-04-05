package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random();


 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {}

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }



 /*
  * Creates a random block given its level and a max depth.
  *
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  if (lvl> maxDepth){
   throw new IllegalArgumentException("lvl is bigger than maxDepth");
  }
  //initialize fields
  this.xCoord = 0;
  this.yCoord = 0;
  this.size = 0;
  this.level = lvl;
  this.maxDepth = maxDepth;
  Block[] noBlocks = {};


  if (lvl == maxDepth) {
   //only leaves can have a color
   int colornumber = gen.nextInt(GameColors.BLOCK_COLORS.length);
   this.color = GameColors.BLOCK_COLORS[colornumber];
   this.children = noBlocks;
  }
  else {
   //if it can subdivide
   double rnumber = gen.nextDouble();

   if (rnumber < Math.exp(-0.25 * level)) {
    this.color = null;
    Block[] bChildren = {new Block(lvl + 1, maxDepth), new Block(lvl + 1, maxDepth), new Block(lvl + 1, maxDepth), new Block(lvl + 1, maxDepth)};
    this.children = bChildren;
   }

   else {
    int colornumber = gen.nextInt(GameColors.BLOCK_COLORS.length);
    this.color = GameColors.BLOCK_COLORS[colornumber];
    this.children = noBlocks;
   }
  }
 }

 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  /*
   * ADD YOUR CODE HERE
   */

  if (size<=0){
   throw new IllegalArgumentException("Invalid size");
  }

  if (size%2==1 && this.level!=this.maxDepth){
   throw new IllegalArgumentException("Invalid size");
  }

  if(this.children.length == 0){
   this.size=size;
   this.xCoord = xCoord;
   this.yCoord = yCoord;
  }
  else{
   this.size=size;
   this.xCoord = xCoord;
   this.yCoord = yCoord;
   this.children[0].updateSizeAndPosition(size/2,this.xCoord+size/2,this.yCoord);
   this.children[1].updateSizeAndPosition(size/2,this.xCoord,this.yCoord);
   this.children[2].updateSizeAndPosition(size/2,this.xCoord,this.yCoord+size/2);
   this.children[3].updateSizeAndPosition(size/2,this.xCoord+size/2,this.yCoord+size/2);
  }
 }




 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  /*
   * ADD YOUR CODE HERE
   */
  ArrayList list = new ArrayList();
  if (this.children.length == 0) {
   list.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
   list.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));
  } else {
   for (int i=0; i<this.children[0].getBlocksToDraw().size();i++){
    list.add(this.children[0].getBlocksToDraw().get(i));
   }
   for (int i=0; i<this.children[1].getBlocksToDraw().size();i++){
    list.add(this.children[1].getBlocksToDraw().get(i));
   }
   for (int i=0; i<this.children[2].getBlocksToDraw().size();i++){
    list.add(this.children[2].getBlocksToDraw().get(i));
   }
   for (int i=0; i<this.children[3].getBlocksToDraw().size();i++){
    list.add(this.children[3].getBlocksToDraw().get(i));
   }
  }
  return list;
 }
 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }



 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  /*
   * ADD YOUR CODE HERE
   */
  if (lvl>this.maxDepth || lvl<this.level){
   throw new IllegalArgumentException("Depth inputted is larger than the maxDepth");
  }

  if (x>this.size+this.xCoord || x<0 || y>this.size+this.yCoord || y<0){
   return null;
  }

  if (this.children.length!=0) {
   if (this.level == lvl){
    return this;
   }
   for (int i = 0; i < this.children.length; i++) {
    if (this.children[i].xCoord <= x && this.children[i].xCoord+ this.children[i].size >= x && this.children[i].yCoord <= y && this.children[i].yCoord+this.children[i].size>=y) {
     return this.children[i].getSelectedBlock(x,y,lvl);
    }
   }
  }
  return this;
 }




 /*
  * Swaps the child Blocks of this Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this Block has no children, do nothing. The swap
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("Integer is neither 0 or 1");
  }

  if (this.children.length != 0) {
   int dir1, dir2, dir3, dir4;
   Block tmp;
   if (direction == 0) {
    dir1 = this.children[0].yCoord;
    dir2 = this.children[1].yCoord;
    dir3 = this.children[2].yCoord;
    dir4 = this.children[3].yCoord;
    this.children[0].yCoord = dir4;
    this.children[3].yCoord = dir1;
    tmp = children[0];
    children[0] =children[3];
    children[3] = tmp;
    this.children[1].yCoord = dir3;
    this.children[2].yCoord = dir2;
    tmp = children[1];
    children[1]=children[2];
    children[2]=tmp;

   } else {
    dir1 = this.children[0].xCoord;
    dir2 = this.children[1].xCoord;
    dir3 = this.children[2].xCoord;
    dir4 = this.children[3].xCoord;
    this.children[0].xCoord = dir2;
    this.children[1].xCoord = dir1;
    tmp = children[0];
    children[0]=children[1];
    children[1]=tmp;
    this.children[3].xCoord = dir3;
    this.children[2].xCoord = dir4;
    tmp = children[3];
    children[3] = children[2];
    children[2] = tmp;
   }
   //Resize and update children within
   for (int i = 0; i<children.length;i++){
    children[i].updateSizeAndPosition(children[i].size,children[i].xCoord,children[i].yCoord);
   }


  } else {
   this.updateSizeAndPosition(size, xCoord, yCoord);
  }
  for (int i = 0; i < this.children.length; i++) {
   this.children[i].reflect(direction);
  }
 }





 /*
  * Rotate this Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("Integer is neither 0 or 1");
  }

  if (this.children.length != 0) {
   Block tmp1,tmp2,tmp3,tmp0;
   if (direction == 0) {
    this.children[0].xCoord = this.children[0].xCoord - this.children[0].size;
    this.children[3].yCoord = this.children[3].yCoord - this.children[3].size;
    this.children[1].yCoord = this.children[1].yCoord + this.children[1].size;
    this.children[2].xCoord =  this.children[2].xCoord+ this.children[2].size;
    tmp0 = children[0];
    tmp1 = children[1];
    tmp2 = children[2];
    tmp3 = children[3];
    children[0]=tmp3;
    children[1]=tmp0;
    children[2]=tmp1;
    children[3]=tmp2;

   } else {
    this.children[0].yCoord = this.children[0].yCoord + this.children[0].size;
    this.children[3].xCoord = this.children[3].xCoord - this.children[3].size;
    this.children[1].xCoord = this.children[1].xCoord + this.children[1].size;
    this.children[2].yCoord = this.children[2].yCoord - this.children[2].size;
    tmp0 = children[0];
    tmp1 = children[1];
    tmp2 = children[2];
    tmp3 = children[3];
    children[0]=tmp1;
    children[1]=tmp2;
    children[2]=tmp3;
    children[3]=tmp0;
   }
   //Resize and update children within
   for (int i = 0; i<children.length;i++){
    children[i].updateSizeAndPosition(children[i].size,children[i].xCoord,children[i].yCoord);
   }


  } else {
   this.updateSizeAndPosition(size, xCoord, yCoord);
  }
  for (int i = 0; i < this.children.length; i++) {
   this.children[i].rotate(direction);
  }
 }



 /*
  * Smash this Block.
  *
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A Block can be smashed iff it is not the top-level Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this Block was smashed and False otherwise.
  *
  */
 public boolean smash() {
  /*
   * ADD YOUR CODE HERE
   */
  if (this.level!=0 && this.level!=this.maxDepth) {
   if (children.length!=0) {
    for (int i = 0; i < this.children.length; i++) {
     this.children[i] = new Block(this.level + 1, this.maxDepth);
    }
    this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
   }
   else{
    Block[] newB = { new Block(this.level+1,this.maxDepth), new Block(this.level+1,this.maxDepth), new Block(this.level+1,this.maxDepth), new Block(this.level+1,this.maxDepth)};
    newB[0].updateSizeAndPosition(size/2,size/2+xCoord,yCoord);
    newB[1].updateSizeAndPosition(size/2,xCoord,yCoord);
    newB[2].updateSizeAndPosition(size/2,xCoord,size/2+yCoord);
    newB[3].updateSizeAndPosition(size/2,size/2+xCoord,size/2+yCoord);
    this.children=newB;
   }
   return true;
  }
  return false;
 }


 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 /*public ArrayList<Color> colorCoord() {
   if (this.children.length == 0) {
    ArrayList alist = new ArrayList();
    ArrayList blist = new ArrayList();
    //add addsfirst, so first list will be reversed
    for (int i = 0; i < this.size * this.size; i++) {
     alist.add(this.color);
    }
    //blist will be in the right order
    for (int i = 0; i < this.size * this.size; i++) {
     blist.add(alist.get(i));
    }
    return blist;
   }
   else{
    ArrayList list = new ArrayList();
    for (int i = 0; i<this.children.length;i++){
     ArrayList templist =this.children[i].colorCoord();
     for (int j =0; j<templist.size();j++){
      list.add(templist.get(j));
     }

    }
    //list length of the area of the area;
    //will be [all top right then all top left then all bot left then all bot right
    return list;
   }
 }*/
 public void list2D(Color[][] cList, int xCoord, int yCoord,int factor){
  int fxcoord = xCoord*factor;
  int fycoord = yCoord*factor;
  if (this.children.length!=0){
   if (fxcoord<this.xCoord+this.size/2){
    if (fycoord<this.yCoord+this.size/2){
     this.children[1].list2D(cList,xCoord,yCoord,factor);
    }
    else{
     this.children[2].list2D(cList,xCoord,yCoord,factor);
    }
   }
   else{
    if(fycoord<this.yCoord+this.size/2){
     this.children[0].list2D(cList,xCoord,yCoord,factor);
    }
    else{
     this.children[3].list2D(cList,xCoord,yCoord,factor);
    }
   }
  }
  else{
   cList[yCoord][xCoord]=this.color;
  }
 }
 public Color[][] flatten() {
  /*
   * ADD YOUR CODE HERE
   */
  int boardDimension = (int) Math.pow(2,this.maxDepth);
  Color[][] mColorList = new Color[boardDimension][boardDimension];
  for (int i = 0; i < boardDimension; i++) {
   for (int j = 0; j < boardDimension; j++) {
    this.list2D(mColorList, j, i,this.size/boardDimension);
   }
  }
  return mColorList;
 }
  /* ArrayList list = new ArrayList();
   Color[] topR = new Color[this.children[0].size*this.children[0].size];
   Color[] topL = new Color[this.children[0].size*this.children[0].size];
   Color[] botL = new Color[this.children[0].size*this.children[0].size];
   Color[] botR = new Color[this.children[0].size*this.children[0].size];
  //initalize all
  for (int i=0; i<this.size;i++) {
   for (int j = 0; j < this.size; j++) {
    mColorList[i][j] = null;
   }
  }
   for (int i=0; i<this.children.length;i++){
    for(Color c: this.children[i].colorCoord()) {
     list.add(c);
    }
   }
   for (int i = 0; i<this.children[0].size *this.children[0].size;i++){
    topR[i]=(Color)list.get(i);
   }
   for (int i = this.children[0].size *this.children[0].size; i<(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size);i++){
    topL[i]=(Color)list.get(i);
   }
   for (int i = (this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size); i<(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size);i++){
    botL[i]=(Color)list.get(i);
   }
   for (int i = (this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size); i<(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size)+(this.children[0].size *this.children[0].size);i++){
    botR[i]=(Color)list.get(i);
   }*/





 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
          , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }

}
