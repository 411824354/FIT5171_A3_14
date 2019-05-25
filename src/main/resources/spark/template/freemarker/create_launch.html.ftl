<#-- @ftlvariable name="rocketName" type="java.lang.String" -->
<#-- @ftlvariable name="launchSite" type="java.lang.String" -->
<#-- @ftlvariable name="launchOrbit" type="java.lang.String" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars: a launch registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a launch information repository - Create Launch">
</head>

<body>

<div id="title_pane">
    <h3>Launch Creation</h3>
</div>

<p>${errorMsg!""}</p>

<div>
    <p>* Fields are required.</p>
</div>
<form name="create_event" action="/launch/create" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>Launch Details</p></div>
        <ol>
            <li>
                <label for="rocketName" class="bold">Rocket Name:*</label>
                <input id="rocketName" name="rocketName" type="text" value="${rocketName!""}">
            </li>
            <li>
                <label for="launchSite" class="bold">launch site:*</label>
                <input id="launchSite" name="launchSite" type="text" value="${launchSite!""}">
            </li>
            <li>
                 <label for="launchOrbit" class="bold">launch Orbit:*</label>
                 <input id="launchOrbit" name="launchOrbit" type="text" value="${launchOrbit!""}">
            </li>
        </ol>
    </div>

    <#if errorMsg?? && errorMsg?has_content>
        <div id="error">
            <p>Error: ${errorMsg}</p>
        </div>
    </#if>
    <div id="buttonwrapper">
        <button type="submit">Create New Launch</button>
        <a href="/">Cancel</a>
    </div>
</form>

</body>