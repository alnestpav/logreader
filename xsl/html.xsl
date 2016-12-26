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
  </style>
  </head>
  <body>
  <div>
    <div id="header"><h1>Log messages</h1></div>
    <xsl:for-each select="/logMessages/logMessage">
      <div id="content">
        <p><b><xsl:value-of select="date"/></b></p>
        <p><xsl:value-of select="message"/></p>
      </div>
    </xsl:for-each>
  </div>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>