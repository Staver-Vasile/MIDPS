<?php include 'database.php'; ?>
<?php
// Get Total Number of Questions
$query = "SELECT * FROM questions";

//Get results

$results = $mysqli->query($query) or die($mysqli->error);

$total = $results->num_rows;

?>

<!DOCTYPE html>
 
<html>
<head>
	<title>PHP Quizzer</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">

</head>
<body>
<header>
	<div class="container">
		<h1>PHP Quizzer</h1>
	</div>
</header>
<main>
	<div class="container">
    <h2>Test Your PHP Knowledge</h2>
    <p>This is a mltiple choice quiz to test your knowledge of PHP</p>
    <ul>
    	<li><strong>Number of Questions: </strong><?php echo $total; ?></li>
    	<li><strong>Type: </strong>Multiple choice</li>
    	<li><strong>Estimate Time: </strong><?php echo $total * .5; ?></li>

    </ul>
    <a href="question.php?n=1" class="start"> Start Quiz </a>
	</div>
</main>
<footer>
	<div class="container">
		Copyright &copy; 2014, PHP Quizzer
	</div>
</footer>

</body>
</html>