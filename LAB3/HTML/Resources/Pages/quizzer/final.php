<?php session_start(); ?>

<!DOCTYPE html>

<html>
<head>
	<title>PHP Quizzer</title>
	<link rel="stylesheet" href="http://localhost:8080/quizzer/phpStyle.css" type="text/css">
</head>
<body>
<header>
	<div class="container">
		<h1>PHP Quizzer</h1>
	</div>
</header>
<main class="finalBG">
	<div class="container">
     <h2>You're Done!</h2>
     <p>Congrats! You have completed the text</p>
     <p>Final Score: <?php echo $_SESSION['score']; ?></p>
       <ul class="menubar">
        	<li class = "menuItems"> <a href="question.php?n=1" class="start"> Take AAAaAAgain </a></li>
        	<li class = "menuItems"> <a href="question.php?n=1" class="start"> asd </a></li>
        	<li class = "menuItems">< <a href="question.php?n=1" class="start"> aaaa </a></li>
    	</ul>

	</div>
</main>
<footer>
	<div class="container">
		Copyright &copy; 2014, PHP Quizzer
	</div>
</footer>

</body>
</html>