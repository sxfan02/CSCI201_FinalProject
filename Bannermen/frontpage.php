<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8" />
    <meta
      name="description"
      content="Bannermen is a card game coming out of USC. This is the website for that game"
    />
    <meta name="keywords" content="Streaming, USC, SC Live, Card Game, Card, Bannermen, Sam" />
    <title>Bannermen</title>
    <link rel="stylesheet" href="bannermen-stylesheet.css" />

    <style type = "text/css">
        /* makes the table look nice  */
        table {
            border-collapse: collapse;
            width: 100%;
            color: maroon;
            font-family: monospace;
            font-size: 25px;
            text-align: left;
        }
        /* set the background color */
        th {
            background-color: maroon;
            color: white;
        }
        /* gives every even row a diff color */
        tr:nth-child(even) {background-color: #f2f2f2;}

    </style>


        <script language=”php”> </script>

  </head>
  <body>
    <!-- header container has the circle picture and the name at the top of the page -->
    <div id="header-container">
      <img src="images/cards/Bannermen-Card-Back.png" 
      alt="twitch logo" id="profile-pic" />
    </div>

    <div class="clearfix"></div>

    <div id="button-container">
      <div class="buttons">
        <div class="home-button">
          <a href="FrontPage.html"> Home </a>
        </div>
        <div class="home-dropdown">
          <a href="#our-mission">About Us</a><br />
          <a href="#charity">Giving Back</a><br />
          <a href="#sponsers">Our Sponsers</a><br />
        </div>
        <div class="rules-button">
          <a href="Rules.html"> Rules</a>
        </div>
        <div class="download-button">
          <a href="Download.html">Download</a>
        </div>
        <div class="contact-button">
          <a href="Contact.html"> Contact Us</a>
        </div>
        <div class="credit-button">
          <a href="Credits.html"> Credits</a>
        </div>
      </div>
    </div>

    <div class="clearfix"></div>

    <div id="main-container">
      <!-- start of main container -->
      <div class = "left-column">
        <!-- Left Column -->
        <h2>About Us</h2>
        <div class="text-bubble-small">
          <!-- the students will be our club when we meet in person <3 -->
          <div class="charity-text">
            <p class="subtitle">Inspiration:</p>
            <P>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Rerum tempora, placeat exercitationem at eligendi vitae dolorum est assumenda, ipsa autem laborum recusandae. Ad ut saepe amet molestiae, voluptatibus repellat repudiandae!</P>
          </div>
        </div>
        <div class="clearfix"></div>
      </div>
      <!-- Right Column -->
      <div class = "right-column">
        <!-- Top 10 List -->
        <h2>Top 10 Bannermen</h2>
        <div class = "text-bubble">
          <div class = charity-text>
            <table> 
            <tr>
            <th>id</th>
            <th>Username</th>
            <th>Wins</th>
            <th>Loses</th>
            </tr>
            <?php
            //connecting to the database -- Not sure if the final arguement is correct
            $conn = mysqli_connect("54.193.145.5", "admin", "TaiLicksNuts!", "CSCI201-Final-Project", "3308");
            //if there is a connection error than abort and report
            if ($conn ->connect_error) {
            die("Connection failed:". $conn-> connect_error);
            }
            //get the id username wins and losses from the table leaderboard
            $sql = "SELECT idleaderboard, username, wins, losses from leaderboard";

            //make the results equal to the information that is brought back to the query
            $result =  $conn-> query($sql);

            $a = 0;

            // if ($result -> num_rows > 0) { //must have more than one row

            $winners = array ();

            while($row = $result -> fetch_assoc() ) {

            $temp = array(
                array(
                    "idleaderboard" => $row["idleaderboard"], 
                    "username"      => $row["username"], 
                    "wins"          => $row["wins"], 
                    "losses"        => $row["losses"] 
                )
            );
            $winners = array_merge($winners, $temp);

            }      

            function sortByOrder($a, $b) {
            return $b['wins'] - $a['wins'];
            }

            function array_sort($array, $on)
            {
            $new_array = array();
            $sortable_array = array();

            if (count($array) > 0) {
                foreach ($array as $k => $v) {
                    if (is_array($v)) {
                        foreach ($v as $k2 => $v2) {
                            if ($k2 == $on) {
                                $sortable_array[$k] = $v2;
                            }
                        }
                    } else {
                        $sortable_array[$k] = $v;
                    }
                }


                foreach ($sortable_array as $k => $v) {
                    $new_array[$k] = $array[$k];
                }
            }

            return $new_array;
            }

            usort($winners, 'sortByOrder');

            // print_r($winners[0]['idleaderboard']);


            // foreach ($winners as $key => $val) {
            for($i = 0; $i < count($winners); $i++) {
            echo "<tr><td>". $winners[$i]["idleaderboard"] . 
                "</td><td>". $winners[$i]["username"] . 
                "</td><td>". $winners[$i]["wins"] . 
                "</td><td>". $winners[$i]["losses"] . 
                    "</td></tr>";
                $a++;
                if ( $a == 10 ) { break; }
            }

            echo "</table>";


            //close connection
            $conn-> close();

            ?>


            </table>  
          </div>
        </div>
        
      </div>
      <div class="clearfix"></div>


      <h2>4 Rival Factions!</h2>
      <div class = "Banner-Section">
        <div class = "card-image-holder">
          <img src="images/banners/Shieldbruddas.png" alt="" width="350">
          <p>The Shields</p>
        </div>
        <div class = "card-image-holder">
          <img src="images/banners/TheKnightsOfTheKnight.png" alt="" width="350">
          <p>The Stars</p>
        </div>
        <div class = "card-image-holder">
          <img src="images/banners/TheKnightsOfTheTreet.png" alt="" width="350">
          <p>The Forest</p>
        </div>
        <div class = "card-image-holder">
          <img src="images/banners/TheMonarchs.png" alt="" width="350">
          <p>The Monarchs</p>
        </div>
        <div class="clearfix"></div>
      </div>

      <!-- Viewers are able to move around cards in this zone -->
      <h2>Take a look at some of the cards!</h2>
      <div class = "playzone">
        <div class = "card-image-holder">
          <img src="images/cards/Monarch-Card-Example.png"
          width = "350px"
          alt="Queen Card"
          class = "draggable"
          style="transform:rotate(-10deg)">
        </div>
        
        <div class = "card-image-holder">
          <img src="images/cards/Queen.png" 
          alt="Queen Card"
          class = "draggable"
          style="transform:rotate(2deg)">
          
        </div>

        <div class = "card-image-holder">
          <img src="images/cards/Shield-Card-Example.png"
          width = "350px"
          style="transform:rotate(-2deg)"
          class = "draggable"
           alt="Merecenary Card">
        </div>
        <div class = "card-image-holder">
          <img src="images/cards/Spy.png"
          style="transform:rotate(2deg)"
          class = "draggable"
          alt="Spy Card">
        </div>
        <div class = "card-image-holder">
          <img src="images/cards/Mercenary.png" 
          style="transform:rotate(2deg)"
          class = "draggable"
          alt="Merecenary Card">
        </div>
        <!-- Second Row -->
        <div class = "card-image-holder">
          <img src="images/cards/Forest-Card-Example.png"
          width = "350px"
          style="transform:rotate(-5deg)"
          class = "draggable"
          alt="King Card"/>
        </div>
        <div class = "card-image-holder">

          <img src="images/cards/King.png" 
          style="transform:rotate(5deg)"
          class = "draggable"
          alt="King Card">
        </div>
        <div class = "card-image-holder">
          <img src="images/cards/Stars-Card-Example.png"
          width = "350px"
          style="transform:rotate(-8deg)"
          class = "draggable"
          alt="Spy Card">
        </div>
      </div>
      <div class="clearfix"></div>

    </div>
    <div class="footer">
      &copy; 2021 Samuel Adams
      <br />
    </div>
    <script
    src="https://code.jquery.com/jquery-3.4.1.min.js"
    integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
    crossorigin="anonymous"
    ></script>
   <script src="https://unpkg.com/draggabilly@2/dist/draggabilly.pkgd.min.js"></script>

    <script>
    var $draggable = $('.draggable').draggabilly({});
    </script>
  </body>
</html>
