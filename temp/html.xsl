<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
  <html>
  <body>
  <h2>Logs</h2>
  <table border="1">
    <tr>
      <th>date</th>
      <th>message</th>
    </tr>
    <xsl:for-each select="/logMessages/logMessage">
    <tr>
      <td><xsl:value-of select="date"/></td>
      <td><xsl:value-of select="message"/></td>
    </tr>
    </xsl:for-each>
  </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>