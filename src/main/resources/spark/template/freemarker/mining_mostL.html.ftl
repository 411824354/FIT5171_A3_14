<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="numberK" type="java.lang.String" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: most launched rocket</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: most launched rocket - Rockets">
</head>

<body>
<div id="title_pane">
    <h3>Most Launched Rocket</h3>
</div>

</div>

<form name="choose display number" action="/mining/mostLaunched" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>choose display number</p></div>
        <ol>
            <li>
                <label for="numberK" class="bold">rocket number:*</label>
                <input id="numberK" name="numberK" type="text" value="${numberK!""}">
            </li>
        </ol>
    </div>

    <div id="buttonwrapper">
        <button type="submit">get Result</button>
        <a href="/">Cancel</a>
    </div>
</form>
</body>
</html>