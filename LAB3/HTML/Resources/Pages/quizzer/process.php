<?php include 'database.php'; ?>
<?php session_start(); ?>
<?php
// Checl to se if score is set



if(isset($_POST)){
$number = $_POST['number'];
$selected_choice = $_POST['choice'];
$next = $number+1;

if($number == 1){
	$_SESSION['score'] = 0;
}
// Get total questions

$query = "SELECT * FROM questions";

//Get result

$result = $mysqli->query($query) or die($mysqli->error);
$total = $result->num_rows;

// Get correct choice

$query = "SELECT * FROM choices WHERE question_number = $number AND is_correct = 1 ";

//Get result

$result = $mysqli->query($query) or die($mysqli->error);

$row = $result->fetch_assoc();

//Set correct choice
$correct_choice = $row['id'];

//Compare
if($correct_choice == $selected_choice){
	//Answer is correct
	$_SESSION['score'] = $_SESSION['score'] + 1;
}

//Check if it is last question
if($number == $total){
header("Location: final.php");
exit();

} else{
header("Location: question.php?n=".$next);
}



}