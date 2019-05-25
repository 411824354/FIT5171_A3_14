<#-- @ftlvariable name="rocketName" type="java.lang.String" -->
<#-- @ftlvariable name="rocketCountry" type="java.lang.String" -->
<#-- @ftlvariable name="providerName" type="java.lang.String" -->
<#-- @ftlvariable name="providerCountry" type="java.lang.String" -->
<#-- @ftlvariable name="providerYear" type="java.lang.String" -->
<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars: a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a rocket information repository - Create Rocket">
</head>

<body>

<div id="title_pane">
    <h3>Rocket Creation</h3>
</div>

<p>${errorMsg!""}</p>

<div>
    <p>* Fields are required.</p>
</div>
<form name="create_event" action="/rocket/create" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>Rocket Details</p></div>
        <ol>
            <li>
                <label for="rocketName" class="bold">Rocket Name:*</label>
                <input id="rocketName" name="rocketName" type="text" value="${rocketName!""}">
            </li>
            <li>
                <label for="rocketCountry" class="bold">rocket country:*</label>
                <input id="rocketCountry" name="rocketCountry" type="text" value="${rocketCountry!""}">
            </li>
            <li>
                 <label for="providerName" class="bold">Provider Name:*</label>
                 <input id="providerName" name="providerName" type="text" value="${providerName!""}">
            </li>
            <li>
                 <label for="providerCountry" class="bold">Provider country:*</label>
                 <input id="providerCountry" name="providerCountry" type="text" value="${providerCountry!""}">
            </li>
            <li>
                  <label for="providerYear" class="bold">Provider year founded:*</label>
                  <input id="providerYear" name="providerYear" type="text" value="${providerYear!""}">
            </li>
        </ol>
    </div>

    <#if errorMsg?? && errorMsg?has_content>
        <div id="error">
            <p>Error: ${errorMsg}</p>
        </div>
    </#if>
    <div id="buttonwrapper">
        <button type="submit">Create New Mission</button>
        <a href="/">Cancel</a>
    </div>
</form>

</body>