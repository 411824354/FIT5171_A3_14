<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="launches" type="java.util.Collection<rockets.model.Launch>" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: a rocket information repository</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a rocket information repository - Rockets">
</head>

<body>
<div id="title_pane">
    <h3>Launch Listing Page</h3>
</div>

<div>
<#if errorMsg?? && errorMsg?has_content>
    <li><h4 class="errorMsg">${errorMsg}</h4></li>
<#elseif launches?? && launches?has_content>
    <ul>
        <#list launches as launch>
            <li><a href="/launch/${launch.id}">${launch.launchDate},${launch.launchVehicle.name}</a></li>
        </#list>

    </ul>
<#else>
    <p>No launch yet in the system. <a href="/launch/create">Create one</a> now!</p>
</#if>

</div>

</body>
</html>