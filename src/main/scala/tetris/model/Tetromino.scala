package tetris.model

import scala.util.Random

object Tetromino {

	//(a)(b)(c);
	//a= list of all the rotation
	//b= list of squares to create a block
	//c= a square
	// Array(x,y)
	// x + 1 = move left
	// y + 1 = move down

	val I = List(List(Array(0,0),Array(0,1),Array(0,2),Array(0,3)),
				 List(Array(0,0),Array(1,0),Array(2,0),Array(3,0))
				 )

	val J = List(List(Array(0,1),Array(1,1),Array(2,1),Array(0,0)),
				 List(Array(0,0),Array(0,1),Array(1,0),Array(2,0)),
				 List(Array(0,0),Array(0,1),Array(1,1),Array(2,1)),
				 List(Array(0,0),Array(0,1),Array(0,2),Array(1,2))
				 )

	val L = List(List(Array(1,0),Array(0,0),Array(0,1),Array(0,2)),
				 List(Array(0,0),Array(1,0),Array(2,0),Array(2,1)),
				 List(Array(0,2),Array(1,2),Array(1,1),Array(1,0)),
				 List(Array(0,0),Array(0,1),Array(1,1),Array(2,1))
				 )

	val T = List(List(Array(0,0),Array(0,1),Array(1,1),Array(0,2)),
				 List(Array(1,0),Array(0,1),Array(1,1),Array(2,1)),
				 List(Array(1,0),Array(0,1),Array(1,1),Array(1,2)),
				 List(Array(0,0),Array(1,0),Array(2,0),Array(1,1))
				 )

	val O = List(List(Array(0,0),Array(0,1),Array(1,0),Array(1,1)),
				 List(Array(0,0),Array(0,1),Array(1,0),Array(1,1))
				 )

	val Z = List(List(Array(0,0),Array(0,1),Array(1,1),Array(1,2)),
				 List(Array(0,1),Array(1,1),Array(1,0),Array(2,0))
				 )

	val S = List(List(Array(1,0),Array(1,1),Array(0,1),Array(0,2)),
				 List(Array(0,0),Array(1,0),Array(1,1),Array(2,1))
				 )

}