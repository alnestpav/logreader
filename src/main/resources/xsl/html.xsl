<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
  <html>
  <head>
  <style>
  div {
	word-wrap: break-word;
    white-space: pre-wrap;
    margin-top: -30px;
  }
  p {
    font-family: courier new;
    font-size: 14;
    margin-top: -30px;
    }
      h1, h2, #author {
      text-align: center;
      }
      .center {
          display: block;
          margin: auto;
          width: 15%;
      }
  </style>
  </head>
  <body>
  <div>
    <div id="header">
        <h1>logreader 1.0.1</h1>
        <div id="company-logo" class="center">
            <img src="siblion_logo.gif" alt="Siblion"></img>
            <h2>Siblion company</h2>
        </div>
        <div id="author">
            <p>Created by Alexander Nesterov</p>
        </div>
        <div id="request">
            <h3>Request:</h3>
            <p>String: <xsl:value-of select="/logMessages/request/string"/></p>
            <p>Location: <xsl:value-of select="/logMessages/request/location"/></p>
            <p>DateIntervals:</p>
        <xsl:for-each select="/logMessages/request/dateIntervals">
            <p>DateFrom: <xsl:value-of select="dateFrom"/></p>
            <p>DateTo: <xsl:value-of select="dateTo"/></p>
        </xsl:for-each>
        <p>FileFormat: <xsl:value-of select="/logMessages/request/fileFormat"/></p>
    </div>
    </div>
      <div id="log messages">
          <h3>Log messages:</h3>
            <xsl:for-each select="/logMessages/logMessage">
                <p><b><xsl:value-of select="date"/></b></p>
                <p><xsl:value-of select="message"/></p>
            </xsl:for-each>
      </div>
  </div>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>