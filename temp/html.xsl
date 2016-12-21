<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
  <html>
  <head>
  <style>
  div {
	margin-top: 1.0em;
	word-wrap:break-word;
  }
  </style>
  </head>
  <body>
  <h2>Logs</h2>
  <div>
    <xsl:for-each select="/logMessages/logMessage">
      <div><p><xsl:value-of select="date"/></p></div>
      <div><p><xsl:value-of select="message"/></p></div>
    </xsl:for-each>
  </div>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>