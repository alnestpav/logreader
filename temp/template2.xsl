<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
 <fo:layout-master-set>
 <fo:simple-page-master page-height="297mm" page-width="210mm"
 margin="5mm 25mm 5mm 25mm" master-name="PageMaster">
 <fo:region-body margin="20mm 0mm 20mm 0mm"/>
 </fo:simple-page-master>
 </fo:layout-master-set>
 <fo:page-sequence master-reference="PageMaster">
 <fo:flow flow-name="xsl-region-body">
 <fo:block>
 <fo:block>Hello World!</fo:block>
 <fo:block>This is the first
 <fo:inline font-weight="bold">SimpleDoc</fo:inline>
 </fo:block>
 </fo:block>
 </fo:flow>
 </fo:page-sequence>
</fo:root>

</xsl:stylesheet>