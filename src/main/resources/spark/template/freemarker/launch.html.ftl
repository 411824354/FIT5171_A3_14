<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="launch" type="rockets.model.Rocket" -->
<#-- @ftlvariable name="participants" type="java.util.Set<flymetomars.model.Person>" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: a launch information repository</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a launch information repository - Rocket">
</head>

<body>
<div id="title_pane">
    <h3>Launch Details Page</h3>
</div>

<div>
<#if errorMsg?? && errorMsg?has_content>
    <li><h4 class="errorMsg">${errorMsg}</h4></li>
<#else>
    <p>Launch details:</p>
    <ul>
        <li>Date: ${launch.launchDate}</li>
        <li>Vehicle: <a href="rocket/${launch.launchVehicle.id}">${launch.launchVehicle.name}</a></li>
        <li>Orbit: ${launch.orbit}</li>
        <li>site: ${launch.launchSite}</li>
    </ul>
</#if>

</div>

</body>
</html>