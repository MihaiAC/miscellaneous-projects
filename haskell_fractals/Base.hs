import Graphics.UI.GLUT
import Graphics.Rendering.OpenGL
import Control.Monad
import Data.Complex -- we are using complex numbers 
--some functions included in Data.Complex: x :+ y = x +yi, magnitude z = sqrt ((Re z)^2 + (Im z)^2)).

 
 ----------------------Settings-------------------------------------------------------------------------------------------------------------------------------------------------------------
type Saturation = GLfloat
shsv = 0.8 :: Saturation -- controls the saturation of the colors used + is included in the interval [0,1]

type ValueH = GLfloat
valueH = 1 :: ValueH -- value that influences the outcome of the coloring + is included in the interval [0,1]

type Chroma = GLfloat
chroma = shsv * valueH -- the chroma is calculated as the product of the saturation and the value

diff = valueH - chroma -- it's used to smooth out the colors

type Smoothcolor = GLfloat -- HSV value that will be converted to RGB

maxiter = 1000 :: Int -- maximum number of iterations - the bigger it is, the clearer the image obtained, but the program will render the image a lot slower.
constant = 0.32 :+ 0.42 :: Complex GLfloat --constant that will directly influence the formula used for generating the image - we will obtain different fractals for different constants
bound = 20 :: GLfloat -- the constant that decides when a value "escapes".
nx = 1.5 :: GLfloat -- controls the zoom of the image as explained before isJulia

wSize = Size 700 700 -- controls the size of the window
width = 1000 -- controls the width of the image
height = 1000 -- controls the height of the image
wxh = (fromIntegral width) * (fromIntegral height) -- constant we'll be needing in our calculations.

--We need to adjust the width and height of certain fractals in order to center the image.
valuewl = 0 :: Int
valuewr = 0 :: Int
valuehu = 0 :: Int
valuehd = 0 :: Int

red1 = 0.3 :: GLfloat
green1 = 1.3 :: GLfloat
blue1 = 1 :: GLfloat 
red2 = 1 :: GLfloat
green2 = 1 :: GLfloat
blue2 = 1 :: GLfloat
red3 = 1 :: GLfloat
green3 = 1 :: GLfloat
blue3 = 1 :: GLfloat
red4 = 1 :: GLfloat
green4 = 1 :: GLfloat
blue4 = 1 :: GLfloat
red5 = 1 :: GLfloat
green5 = 1 :: GLfloat
blue5 = 1 :: GLfloat
red6 = 1 :: GLfloat
green6 = 1 :: GLfloat
blue6 = 1 :: GLfloat -- these variables allow us a greater freedom in coloring our fractal - must be bigger than 0, preferrably in the interval [0..1]
--Selects the function used to do the iterations.
function :: Complex GLfloat -> Complex GLfloat
function = f1

--Different functions with their settings for different fractals (specified in brackets)
f1 :: Complex GLfloat -> Complex GLfloat
f1 z = z ^ 2 
-- Method of coloring with exp (nx = 2) : -0.835 + 0.2321i (constant1), (-0.8) :+ 0.156 (constant2) , 0.285 :+ 0.01 (constant3)
-- Method of coloring with log (nx = 1) : -0.79 :+ 0.15 (constant 1), .28 + .008 (constant2), (-0.163) :+ 1.04 (constant3), (-0.12) :+ (-0.77) (constant 4) (-0.75) :+ (0.1) (constant5 - r1 0.3 g1 1.3 b1 1), 0.21 :+ 0.58 (constant6), 
-- 0.15 :+ 0.58 (constant 7) 0.28 :+ 0.49 (constant 8), 0.32 :+ 0.42 (constant 9), (-0.57) :+ 0.48 (constant 10), (-0.86) :+ 0.21 (constant 11)

--Failed attempts (with examples) :
f2 :: Complex GLfloat -> Complex GLfloat
f2 z = z ^ 5 -- constant = 0.544 :+ 0 bound = 8 nx = 2 shsv = 0.8 valueH = 1 maxiter = 1000 width = 1000 height = 1000 value__ = 0 red 1 green 0.8 blue 0.5

f3 :: Complex GLfloat -> Complex GLfloat
f3 z = sqrt (sinh (z ^ 2)) -- constant 0.544 :+ 0 bound = 5 nx = 2 shsv = 0.8 valueH = 1 maxiter = 1000 height = 1000 value__ = 0 red 1 green 1 blue 1
--constant 0.065,0.122i
f4 :: Complex GLfloat -> Complex GLfloat
f4 z = (z ^ 2 / 2) + 0.26 * (z ^ 4)

--OBS : keep constant 0 :+ 0 and bound 20 and maxiter 50 for the next ones.
g5 :: Complex GLfloat -> Complex GLfloat
g5 z = z ^ 5 - 1

g5' :: Complex GLfloat -> Complex GLfloat
g5' z = 5 * (z ^ 4)

f5 :: Complex GLfloat -> Complex GLfloat
f5 z | magnitude z > 0.0001 = z - (g5 z) / (g5' z)
     | otherwise = (0 :+ 0)

g6 :: Complex GLfloat -> Complex GLfloat
g6 z = sin z

g6' :: Complex GLfloat -> Complex GLfloat
g6' z = cos z
--f6 creates two vertical "lines"
f6 :: Complex GLfloat -> Complex GLfloat
f6 z | magnitude (cos z) > 0.0001 = z - ((g6 z) / (g6' z))
     | otherwise = f1 z

f7 :: Complex GLfloat -> Complex GLfloat
f7 z | magnitude (z) > 0.00001 = (1 + 3 * z ^ 4) / (4 * z ^ 3)
     | otherwise = f1 z

f8 :: Complex GLfloat -> Complex GLfloat
f8 z | magnitude (cos z) > 0.005 = z - (sin z) / (cos z)
     | otherwise = f1 z

c1f9 = 0 :+ (-1) :: Complex GLfloat
c2f9 = 0.23 :+ 0.678 :: Complex GLfloat

f9 :: Complex GLfloat -> Complex GLfloat
f9 z = 1 + c1f9 * z ^ 5
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


----------------------OpenGL stuff----------------------------------------------------------------------------------------------------------------------------------------------------------
main :: IO ()
main = do
  (_progName, _args) <- getArgsAndInitialize
  _window <- createWindow "(Not Responding) (Not Responding) (Not Responding) Julia Set (Not Responding) (Not Responding)" --just a way to vent my frustration, ignore it
  keyboardMouseCallback $= Just keyboardMouse
  initialDisplayMode $= [DoubleBuffered]
  windowSize $= wSize
  reshapeCallback $= Just reshape
  displayCallback $= display
  actionOnWindowClose $= MainLoopReturns
  mainLoop

keyboardMouse :: KeyboardMouseCallback
keyboardMouse _key _state _modifiers _position = return ()

display = do
   clear [ColorBuffer]
   loadIdentity
   scale 0.001 0.001 (0.2::GLfloat) -- rescales the picture
   forM_ points $ \(x,y,z,t) -> -- plots the points and their color
    preservingMatrix $ do
      juliaa points
      swapBuffers
   flush

juliaa :: [(GLfloat,GLfloat,GLfloat,Color3 GLfloat)] -> IO ()
juliaa asdf = renderPrimitive Points $ do  
            mapM_ drawing asdf
                where 
                  drawing (x,y,z,c) = do
                  color c
                  vertex $ Vertex3 x y z

reshape :: ReshapeCallback
reshape size = do
  viewport $= (Position 0 0, size)
  postRedisplay Nothing 


--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


---------Functions used to generate the fractal-based image---------------------------------------------------------------------------------------------------------------------------------

-- This function decides if a point is going to be colored.
funcRec :: Complex GLfloat -> Int -> Smoothcolor -> (Bool,Smoothcolor) 
funcRec z 0 sm = (False, 0)
funcRec z n sm = if (magnitude fz < bound) then funcRec (fz) (n-1) (sm) else (True, (fromIntegral (maxiter - n) - logBase 2 ((log (magnitude fz)) / (log bound))) / fromIntegral (maxiter)) where fz = function z + constant
--A smooth coloring method (will work for the majority of our functions)
-- funcRec z n sm = if ((magnitude z) < bound) then funcRec ((function z) + constant) (n-1) (sm + exp(-magnitude z)) else (True, (sm / fromIntegral (maxiter - n))) 

--Calculates the mod 2 of a GLfloat - the function is needed for converting HSV to RGB in coloring.
modulo2 :: Smoothcolor -> Smoothcolor
modulo2 x | x < 2 = x
          | x > 2 = modulo2 (x-2) 

--A small transformation to further smooth our coloring.
intermvalueX :: Smoothcolor -> GLfloat
intermvalueX sm = chroma * (1 - abs ((modulo2 sm) - 1))

--Converts HSV to RGB.
--We can have fun by making one of those lines black in order to see exactly which points are affected by the coloring.
coloring :: Smoothcolor -> Color3 GLfloat
coloring u | u == 0 = Color3 0 0 0
           | u >= 0 && u < 1 = Color3 ((valueH) * red1) (((intermvalueX u) + diff) * green1) ((diff) * blue1)
           | u >= 1 && u < 2 = Color3 (((intermvalueX u) + diff) * red2) ((valueH) * green2) ((diff) * blue2)
           | u >= 2 && u < 3 = Color3 ((diff) * red3) ((valueH) * green3) (((intermvalueX u) + diff) * blue3)
           | u >= 3 && u < 4 = Color3 ((diff) * red4) (((intermvalueX u) + diff) * green4) ((valueH) * blue4)
           | u >= 4 && u < 5 = Color3 (((intermvalueX u) + diff) * red5) ((diff) * green5) ((valueH) * blue5)
           | u >= 5 && u < 6 = Color3 ((valueH) * red6) ((diff) * green6) (((intermvalueX u) + diff) * blue6)

-- nx - bigger number => zoom out the picture, smaller => zoom in (I've done this before I discovered I could scale the picture in the OpenGL panel). A more correct name of the function would be isNotJulia, but we'll stick to this notation.
isJulia :: Complex GLfloat -> (Bool,Smoothcolor) 
isJulia (x :+ y) = funcRec ((nx * x / (fromIntegral width)) :+ (nx * y / (fromIntegral height))) maxiter 0

-- Here we evaluate each point in the rectangle (-width, -height) (width, height) and color it if it's not in the Julia Set.
juliaP :: [(GLfloat,GLfloat,Smoothcolor)]
juliaP = [(x,y,snd j) | x <- [fromIntegral (-width + valuewl)..fromIntegral (width + valuewr)], y <- [fromIntegral (-height + valuehd)..fromIntegral (height + valuehu)], let j = isJulia (x :+ y), fst j]

--The points we have to plot and their color - "black" if the point is in the Julia Set, colored if it's not (if it escapes after a number of iterations)
points :: [(GLfloat,GLfloat,GLfloat,Color3 GLfloat)]
points =  [(x,y,0,(coloring (sm * 6))) | (x,y,sm) <- juliaP]
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

----------------------------References------------------------------------------------------------------------------------------------------------------------------------------------------
--https://en.wikipedia.org/wiki/HSL_and_HSV                                                   - for information on how HSV works and how to convert it to RGB;
--http://stackoverflow.com/questions/369438/smooth-spectrum-for-mandelbrot-set-rendering      - one smooth coloring algorithm for Julia Sets;
--http://hackage.haskell.org/package/GLUT-2.7.0.3/docs/Graphics-UI-GLUT-Callbacks-Window.html - information about OpenGL.
--https://wiki.haskell.org/OpenGLTutorial1#Lines                                              -  
--https://wiki.haskell.org/OpenGLTutorial2                                                    - tutorial on OpenGL.
--http://www.juliasets.dk/Pictures_of_Julia_and_Mandelbrot_sets.htm                           - useful information about Julia Sets, Mandelbrot fractals and Newton fractals.
--http://www.karlsims.com/julia.html                                                          - interesting Julia Fractals
